package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.DTO.ImageDTO;
import com.example.onlybunsbe.DTO.LocationDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.model.Image;
import com.example.onlybunsbe.model.Location;
import com.example.onlybunsbe.model.Post;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post toPostEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setDescription(postDTO.getDescription());
        post.setCreatedAt(Instant.from(LocalDateTime.now())); // ili prema postDTO, ako je potrebno

        // Mapiranje ImageDTO na Image
        Image image = new Image();
        image.setPath(postDTO.getImage().getPath());
        post.setImage(image);

        // Mapiranje LocationDTO na Location
        Location location = new Location();
        location.setLatitude(postDTO.getLocation().getLatitude());
        location.setLongitude(postDTO.getLocation().getLongitude());
        post.setLocation(location);

        // Postavite dodatna polja po potrebi
        return post;
    }

    public PostDTO toPostDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setCreatedAt(post.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());

        // Mapiranje Image na ImageDTO
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setPath(post.getImage().getPath());
        dto.setImage(imageDTO);

        // Mapiranje Location na LocationDTO
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(post.getLocation().getLatitude());
        locationDTO.setLongitude(post.getLocation().getLongitude());
        dto.setLocation(locationDTO);

        dto.setLikeCount(post.getLikes().size());

        if (post.getComments() != null) {
            List<CommentDTO> commentDTOs = post.getComments().stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId(comment.getId());
                commentDTO.setPostId(post.getId());
                commentDTO.setUserId(comment.getUser().getId());
                commentDTO.setContent(comment.getContent());
                commentDTO.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
                commentDTO.setUserName(comment.getUser().getUsername());
                return commentDTO;
            }).collect(Collectors.toList());
            dto.setComments(commentDTOs);
        }

        return dto;
    }
}
