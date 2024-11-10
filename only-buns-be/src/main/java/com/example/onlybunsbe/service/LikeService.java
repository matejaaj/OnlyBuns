package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.LikeDTO;
import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.LikeRepository;
import com.example.onlybunsbe.repository.PostRepository;
import com.example.onlybunsbe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean likePost(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);
        System.out.print("Pronadjen post = " + post + " user = " + user);
        if (post.isPresent() && user.isPresent()) {
            if (likeRepository.existsByPostAndUser(post.get(), user.get())) {
                return false; // Već lajkovano
            }
            Like like = new Like();
            like.setPost(post.get());
            like.setUser(user.get());
            like.setLikedAt(Instant.now());
            likeRepository.save(like);
            return true;
        }
        return false;
    }

    @Transactional
    public void removeLikesByPost(Post post) {
        likeRepository.deleteByPost(post);
    }

    @Transactional
    public List<LikeDTO> getAllLikes() {
        return likeRepository.findAll().stream().map(like -> {
            LikeDTO dto = new LikeDTO();
            dto.setPostId(Long.valueOf(like.getPost().getId()));
            dto.setUserId(like.getUser().getId());
            return dto;
        }).collect(Collectors.toList());
    }
}
