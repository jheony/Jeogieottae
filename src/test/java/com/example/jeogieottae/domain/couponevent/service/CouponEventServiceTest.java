package com.example.jeogieottae.domain.couponevent.service;

import com.example.jeogieottae.domain.couponevent.entity.CouponEvent;
import com.example.jeogieottae.domain.couponevent.repository.CouponEventRepository;
import com.example.jeogieottae.domain.usercoupon.repository.UserCouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.LongStream;

@SpringBootTest
class CouponEventServiceTest {

    @Autowired
    private CouponEventService couponEventService;

    @Autowired
    private CouponEventRepository couponEventRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    void 동일_유저_동시_쿠폰_발급_테스트() throws InterruptedException {

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

        System.out.println("남은 수량 = " + event.getAvailableQuantity());
        System.out.println("userId=80 발급된 쿠폰 수 = " + issuedCount);
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
        System.out.println("남은 수량 = " + event.getAvailableQuantity());
        System.out.println("전체 발급된 쿠폰 수 = " + issuedCount);

        assert issuedCount == 5;
        assert event.getAvailableQuantity() == 0;
    }
}