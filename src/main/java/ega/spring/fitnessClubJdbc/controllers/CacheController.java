package ega.spring.fitnessClubJdbc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/clear-redis-cache")
    public String clearRedisCache() {
        try {
            redisTemplate.delete("allPersons");
            return "Кэш для ключа 'allPersons' очищен";
        } catch (Exception e) {
            return "Ошибка при очистке кэша Redis: " + e.getMessage();
        }
    }
}
