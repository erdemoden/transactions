package com.woh.transactions.services;

import com.woh.transactions.util.RedisCacheStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheStarter implements CommandLineRunner {

    private final RedisCacheStore redisCacheStore;
    @Value("${transfer.rediskey}")
    private String REDIS_KEY;
    @Override
    public void run(String... args) throws Exception {
        redisCacheStore.putWithExpiration(REDIS_KEY,"secretKey", 3456000);
    }
    @Scheduled(fixedRate = 3600000)
    public void refreshKeyIfNeeded() {
        Object existingValue = redisCacheStore.get(REDIS_KEY);
        if (existingValue == null) {
            redisCacheStore.putWithExpiration(REDIS_KEY, "secretKey", 3456000);
            log.info("Redis anahtarı oluşturuldu ve 40 gün boyunca geçerli olacak şekilde ayarlandı.");
        } else {
            log.info("Redis anahtarı zaten mevcut, yenileme işlemi yapılmadı.");
        }
    }
}
