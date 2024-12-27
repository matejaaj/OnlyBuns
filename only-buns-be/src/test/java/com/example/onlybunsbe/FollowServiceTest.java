package com.example.onlybunsbe;

import com.example.onlybunsbe.model.Follow;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.FollowRepository;
import com.example.onlybunsbe.repository.UserRepository;
import com.example.onlybunsbe.service.FollowService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    private Long followerId;
    private Long followedUserId;

    @BeforeEach
    void setUp() {
        // Očisti sve prethodne podatke iz baza
        followRepository.deleteAll();
        userRepository.deleteAll();

        // Kreiraj i sačuvaj testnog korisnika koji prati
        User follower = User.builder()
                .username("testUser")
                .password("password") // Ovde možeš koristiti enkodovanje ako je potrebno
                .email("testuser@example.com")
                .firstName("Test")
                .lastName("User")
                .enabled(true)
                .build();

        User savedFollower = userRepository.save(follower);
        followerId = savedFollower.getId(); // Čuvanje ID-a za testove

        // Kreiraj i sačuvaj korisnika koji se prati
        User followedUser = User.builder()
                .username("userToFollow")
                .password("password") // Ovde možeš koristiti enkodovanje ako je potrebno
                .email("userToFollow@example.com")
                .firstName("Follow")
                .lastName("User")
                .enabled(true)
                .build();

        User savedFollowedUser = userRepository.save(followedUser);
        followedUserId = savedFollowedUser.getId(); // Čuvanje ID-a za testove
    }

    @Test
    void testConcurrentFollowWithoutUserModelChange() throws InterruptedException {
        int threadCount = 1000; // Broj niti koje pokušavaju da prate korisnika
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        String currentUsername = "testUser";

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    followService.followUser(currentUsername, followedUserId);
                } catch (RuntimeException e) {
                    System.out.println("Conflict detected: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Sačekaj da sve niti završe
        executorService.shutdown();

        List<Follow> follows = followRepository.findAll();
    }
}
