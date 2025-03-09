package com.example.onlybunsbe.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private static final int LIMIT = 50; // Maksimalno 50 praćenja u minuti
    private final Map<String, UserFollowActivity> userFollowActivities = new ConcurrentHashMap<>();

    public boolean isAllowed(String username) {
        Instant now = Instant.now();
        userFollowActivities.computeIfAbsent(username, key -> new UserFollowActivity());

        UserFollowActivity activity = userFollowActivities.get(username);

        synchronized (activity) {
            // Ako je prošlo više od 60 sekundi, resetuj brojač
            if (now.isAfter(activity.getWindowStart().plusSeconds(60))) {
                activity.reset(now);
            }

            // Proveri da li korisnik premašuje limit
            if (activity.getCount().get() >= LIMIT) {
                return false; // Prekoračenje limita
            }

            activity.getCount().incrementAndGet();
            return true; // Dozvoljeno praćenje
        }
    }

    // Unutrašnja klasa za praćenje aktivnosti korisnika
    private static class UserFollowActivity {
        private AtomicInteger count;
        private Instant windowStart;

        public UserFollowActivity() {
            this.count = new AtomicInteger(0);
            this.windowStart = Instant.now();
        }

        public AtomicInteger getCount() {
            return count;
        }

        public Instant getWindowStart() {
            return windowStart;
        }

        public void reset(Instant now) {
            this.count.set(0);
            this.windowStart = now;
        }
    }
}
