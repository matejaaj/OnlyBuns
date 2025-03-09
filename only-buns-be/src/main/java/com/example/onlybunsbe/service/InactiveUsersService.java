package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InactiveUsersService {

    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public InactiveUsersService(UserService userService, LikeService likeService, CommentService commentService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.emailSenderService = emailSenderService;
    }

    @Scheduled(cron = "*/30 00 15 * * *")
    private void NotifyInactiveUsers() {
        System.out.println("NotifyInactiveUsers metoda je započela izvršavanje: " + Instant.now());

        List<User> users = userService.getUsersInactiveForMoreThan7Days();

        for (User user : users) {
            Instant lastLoginDate = user.getLastLoginDate();
            StringBuilder emailContent = new StringBuilder();

            List<Post> userPosts = user.getPosts();

            for (Post post : userPosts) {
                long likeCount = likeService.getLikeCountAfterDate(post, lastLoginDate);
                long commentCount = commentService.getCommentCountAfterDate(post, lastLoginDate);

                if (likeCount > 0) {
                    emailContent.append("Post: ").append(post.getDescription()).append(" has ").append(likeCount).append(" new likes.\n");
                }
                if (commentCount > 0) {
                    emailContent.append("Post: ").append(post.getDescription()).append(" has ").append(commentCount).append(" new comments.\n");
                }
            }

            if (!emailContent.isEmpty()) {
                emailSenderService.sendEmail(
                        user.getEmail(),
                        "You have new interactions on your posts!",
                        emailContent.toString()
                );
                System.out.println("Poslat email korisniku: " + user.getEmail());
            } else {
                System.out.println("Nema novih interakcija za korisnika: " + user.getEmail());
            }
        }

        System.out.println("NotifyInactiveUsers metoda je završila izvršavanje: " + Instant.now());
    }
}
