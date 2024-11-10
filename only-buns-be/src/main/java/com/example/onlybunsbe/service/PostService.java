package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.dtomappers.PostMapper;
import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Image;
import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.findById(id).map(postMapper::toPostDTO);
    }

    @Transactional
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toPostDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<PostDTO> createPost(PostDTO postDTO, MultipartFile image) throws IOException {
        Image imageEntity = imageService.createImageEntity(image);


        postDTO.getImage().setPath(imageEntity.getPath());


        // Kreiraj Post entitet
        Post post = postMapper.toPostEntity(postDTO);
        post.setComments(new ArrayList<Comment>());
        post.setLikes(new ArrayList<Like>());
        post.setImage(imageEntity);

        Post savedPost = postRepository.save(post);
        return Optional.of(postMapper.toPostDTO(savedPost));
    }
}
