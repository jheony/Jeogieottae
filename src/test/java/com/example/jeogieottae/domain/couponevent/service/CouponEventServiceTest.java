package com.example.jeogieottae.domain.couponevent.service;

import com.example.jeogieottae.domain.coupon.enums.CouponEventStatus;
import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import com.example.jeogieottae.domain.couponevent.repository.CouponEventRepository;
import com.example.jeogieottae.domain.usercoupon.repository.UserCouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.LongStream;

@Slf4j
@SpringBootTest
class CouponEventServiceTest {

    @Autowired
    private CouponEventService couponEventService;

    @Autowired
    private CouponEventRepository couponEventRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @BeforeEach
    void setUp() {
        userCouponRepository.deleteAll();

        CouponEvent event = couponEventRepository.findById(3L)
                .orElseThrow();

        ReflectionTestUtils.setField(event, "availableQuantity", 5L);
        ReflectionTestUtils.setField(event, "status", CouponEventStatus.ONGOING);

        couponEventRepository.saveAndFlush(event);
    }

    @Test
    void 동일_유저_동시_쿠폰_발급_테스트_락없음() throws InterruptedException {

        //given
        int threadCount = 10;
        //동시에 10명이서 작업을 해라
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long userId = 80L;
        Long couponEventId = 3L;

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponEventService.issueCouponEvent(userId, couponEventId);
                } catch (Exception e) {
                    // 중복 발급 / 수량 초과
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        CouponEvent event = couponEventRepository.findById(couponEventId).get();
        long issuedCount = userCouponRepository.countByUserIdAndCouponId(
                userId, event.getCoupon().getId()
        );
    }

    @Test
    void 비관적락_적용_후_동일유저_동시발급_테스트() throws InterruptedException {

        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long userId = 80L;
        Long couponEventId = 3L;

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponEventService.issueCouponEvent(userId, couponEventId);
                } catch (Exception e) {
                    //이미 발급 / 수량 초과
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        CouponEvent event = couponEventRepository.findById(couponEventId)
                .orElseThrow();

        long issuedCount =
                userCouponRepository.countByUserIdAndCouponId(
                        userId,
                        event.getCoupon().getId()
                );

        assert issuedCount == 1;
        assert event.getAvailableQuantity() == 4;
    }

    @Test
    void 여러_유저_동시_쿠폰_발급_테스트() throws InterruptedException {

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long couponEventId = 3L;
        List<Long> userIdList = LongStream.rangeClosed(1L, threadCount)
                .boxed()
                .toList();

        //when
        for(long userId : userIdList) {
            executorService.submit(() -> {
                try {
                    couponEventService.issueCouponEvent(userId, couponEventId);
                }  catch (Exception e) {
                    //이미 발급 / 수량 초과
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        CouponEvent event = couponEventRepository.findById(couponEventId)
                .orElseThrow();
        long issuedCount = userCouponRepository.countByCouponId(
                event.getCoupon().getId()
        );
        log.info("[여러 유저 동시 발급 테스트] 남은 수량 = {}", event.getAvailableQuantity());
        log.info("[여러 유저 동시 발급 테스트] 전체 발급된 쿠폰 수 = {}", issuedCount);

        assert issuedCount == 5;
        assert event.getAvailableQuantity() == 0;
    }

    @Test
    void redis_분산락_테스트() throws InterruptedException {

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long couponEventId = 3L;
        List<Long> userIdList = LongStream.rangeClosed(1L, threadCount)
                .boxed()
                .toList();
        for(long userId : userIdList) {
            executorService.submit(() -> {
                try {
                    couponEventService.issueCouponEvent(userId, couponEventId);
                } catch (Exception e) {
                    //락 획득 실패 / 수량 초과
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        CouponEvent event = couponEventRepository.findById(couponEventId)
                .orElseThrow();

        long issuedCount = userCouponRepository.countByCouponId(
                event.getCoupon().getId()
        );

        log.info("[Redis 분산락 테스트] 남은 수량 = {}",event.getAvailableQuantity());
        log.info("[Redis 분산락 테스트] 전체 발급된 쿠폰 수 = {}",issuedCount);

        // 검증
        assert issuedCount == 1;
        assert event.getAvailableQuantity() == 4;
    }
}