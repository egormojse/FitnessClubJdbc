package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/all")
    public Set<String> getAllSessionKeys() {
        return redisTemplate.keys("spring:session:sessions:*");
    }

    @GetMapping("/ttl/{sessionId}")
    public String getSessionTTL(@PathVariable String sessionId) {
        String key = "spring:session:sessions:" + sessionId;

        Long ttl = redisTemplate.getExpire(key);

        if (ttl == null) {
            return "Session not found or has expired";
        }
        return ttl > 0 ? "Time to live for session " + sessionId + " is " + ttl + " seconds" : "Session is persistent or already expired";
    }
}
