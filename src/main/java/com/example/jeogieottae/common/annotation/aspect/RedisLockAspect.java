package com.example.jeogieottae.common.annotation.aspect;

import com.example.jeogieottae.common.annotation.RedisLock;
import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.redis.service.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private final RedisLockService lockService;

    @Around("@annotation(redisLock)")
    public Object run(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        String keyPreFix = redisLock.key();
        String value = UUID.randomUUID().toString();

        Object[] args = joinPoint.getArgs();
        Object couponEventId = args[1];

        String key = keyPreFix + couponEventId;

        boolean locked = lockService.tryLock(key, value, redisLock.timeout());

        if (!locked) {
            log.info("락획득 실패 : {}", Thread.currentThread().getName());
            throw new CustomException(ErrorCode.COUPON_EVENT_CONFLICT);
        }

        try {
            log.info("락획득 성공 : {}", Thread.currentThread().getName());
            return joinPoint.proceed();
        } finally {
            lockService.unlock(key, value);
        }
    }
}
