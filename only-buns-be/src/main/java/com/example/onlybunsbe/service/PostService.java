package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.DTO.ImageDTO;
import com.example.onlybunsbe.DTO.LocationDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.infrastructure.messaging.RabbitMQPublisher;
import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.repository.*;
import com.example.onlybunsbe.dtomappers.PostMapper;
import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Image;
import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.model.Post;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private ImageService imageService;
    private UserRepository userRepository;
    private LikeRepository likeRepository;
    private CommentRepository commentRepository;
    private LocationRepository locationRepository;
    private final RabbitMQPublisher rabbitMQPublisher; // Dodato
    @Transactional
    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.findById(id).map(postMapper::toPostDTO);
    }

    @Transactional
    public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(postMapper::toPostDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<PostDTO> createPost(PostDTO postDTO, MultipartFile image) throws IOException {
        // Create Image entity
        Image imageEntity = imageService.createImageEntity(image);
        postDTO.getImage().setPath(imageEntity.getPath());

        // Create Post entity from DTO
        Post post = postMapper.toPostEntity(postDTO);
        post.setComments(new ArrayList<>());
        post.setLikes(new ArrayList<>());
        post.setImage(imageEntity);

        // Ensure Location is persisted before setting to Post (if Location is used)
        if (post.getLocation() != null && post.getLocation().getId() == null) {
            locationRepository.save(post.getLocation()); // Persist Location first if new
        }

        // Save Post
        Post savedPost = postRepository.save(post);
        return Optional.of(postMapper.toPostDTO(savedPost));
    }
    // Metoda za lajkovanje objave
    @Transactional
    public boolean likePost(Long postId, Long userId) {
        var post = postRepository.findById(postId);
        var user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            // Proveri da li je korisnik već lajkovao post
            if (likeRepository.existsByPostAndUser(post.get(), user.get())) {
                return false; // Već lajkovano
            }
            Like like = new Like();
            like.setUser(user.get());
            like.setPost(post.get());
            like.setLikedAt(Instant.now());
            likeRepository.save(like);
            return true;
        }
        return false;
    }

    // Metoda za dodavanje komentara na objavu
    @Transactional
    public Optional<CommentDTO> addComment(Long postId, Long userId, String content) {
        var post = postRepository.findById(postId);
        var user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUser(user.get());
            comment.setPost(post.get());
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);

            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(Long.valueOf(comment.getId()));
            commentDTO.setContent(content);
            commentDTO.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
            commentDTO.setUserName(user.get().getUsername());
            return Optional.of(commentDTO);
        }
        return Optional.empty();
    }

    // Metoda za ažuriranje objave
    @Transactional
    public Optional<PostDTO> updatePost(Long postId, Long userId, String newDescription) {
        var post = postRepository.findById(postId);

        if (post.isPresent() && post.get().getUser().getId().equals(userId)) {
            post.get().setDescription(newDescription);
            postRepository.save(post.get());
            return getPostById(postId);
        }
        return Optional.empty();
    }

    // Metoda za brisanje objave
    @Transactional
    public boolean deletePost(Long postId, Long userId) {
        var post = postRepository.findById(postId);

        if (post.isPresent() && post.get().getUser().getId().equals(userId)) {
            // Uklanjanje lajkova i komentara vezanih za post
            likeRepository.deleteByPost(post.get());
            commentRepository.deleteByPost(post.get());
            postRepository.delete(post.get());
            return true;
        }
        return false;
    }

    public PostDTO markPostAsEligible(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        post.setEligibleForAd(true);
        Post updatedPost = postRepository.save(post);

        rabbitMQPublisher.sendPostMessage(
                post.getDescription(),
                post.getUser().getUsername(),
                post.getCreatedAt().toString()
        );

        return postMapper.toPostDTO(updatedPost);
    }

}
