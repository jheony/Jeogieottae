package com.example.jeogieottae.domain.reservation.service;

import com.example.jeogieottae.domain.accommodation.entity.Accommodation;
import com.example.jeogieottae.domain.accommodation.enums.AccommodationType;
import com.example.jeogieottae.domain.accommodation.enums.City;
import com.example.jeogieottae.domain.accommodation.repository.AccommodationRepository;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationRequest;
import com.example.jeogieottae.domain.reservation.dto.CreateReservationResponse;
import com.example.jeogieottae.domain.reservation.entity.Reservation;
import com.example.jeogieottae.domain.reservation.repository.ReservationRepository;
import com.example.jeogieottae.domain.room.entity.Room;
import com.example.jeogieottae.domain.room.repository.RoomRepository;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        User user = User.createLocal("asdf@asdf.com", "테스트유저", "1234");
        Accommodation accommodation = new Accommodation("accommodation1", AccommodationType.HOTEL, City.CHEONAN);
        Room room = new Room("room", 100000L, accommodation, null);

        userRepository.save(user);
        accommodationRepository.save(accommodation);
        roomRepository.save(room);

    }

    @Test
    void createReservation_raceConditionTest() {
        Long userId1 = 1L;
        Long roomId = 1L;

        CreateReservationRequest request = new CreateReservationRequest(
                roomId,
                null,
                LocalDateTime.of(2026, 1, 19, 14, 0),
                LocalDateTime.of(2026, 1, 20, 11, 0),
                2L
        );

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CyclicBarrier barrier = new CyclicBarrier(5);

        Callable<CreateReservationResponse> task1 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task2 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task3 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };
        Callable<CreateReservationResponse> task4 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task5 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task6 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };
        Callable<CreateReservationResponse> task7 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task8 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };

        Callable<CreateReservationResponse> task9 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };
        Callable<CreateReservationResponse> task10 = () -> {
            barrier.await();
            Thread.sleep(1000);
            return reservationService.createReservation(userId1, request);
        };


        Future<CreateReservationResponse> future1 = executor.submit(task1);
        Future<CreateReservationResponse> future2 = executor.submit(task2);
        Future<CreateReservationResponse> future3 = executor.submit(task3);
        Future<CreateReservationResponse> future4 = executor.submit(task4);
        Future<CreateReservationResponse> future5 = executor.submit(task5);
        Future<CreateReservationResponse> future6 = executor.submit(task6);
        Future<CreateReservationResponse> future7 = executor.submit(task7);
        Future<CreateReservationResponse> future8 = executor.submit(task8);
        Future<CreateReservationResponse> future9 = executor.submit(task9);
        Future<CreateReservationResponse> future10 = executor.submit(task10);


        try {
            CreateReservationResponse response1 = future1.get();
            System.out.println("Reservation 1: " + response1);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task1 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response2 = future2.get();
            System.out.println("Reservation 2: " + response2);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task2 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response3 = future3.get();
            System.out.println("Reservation 3: " + response3);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task3 failed: " + e.getCause().getMessage());
        }
        try {
            CreateReservationResponse response4 = future4.get();
            System.out.println("Reservation 4: " + response4);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task4 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response5 = future5.get();
            System.out.println("Reservation 5: " + response5);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task5 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response6 = future6.get();
            System.out.println("Reservation 6: " + response6);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task6 failed: " + e.getCause().getMessage());
        }
        try {
            CreateReservationResponse response7 = future7.get();
            System.out.println("Reservation 7: " + response7);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task7 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response8 = future8.get();
            System.out.println("Reservation 8: " + response8);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task8 failed: " + e.getCause().getMessage());
        }

        try {
            CreateReservationResponse response9 = future9.get();
            System.out.println("Reservation 9: " + response9);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task9 failed: " + e.getCause().getMessage());
        }
        try {
            CreateReservationResponse response10 = future10.get();
            System.out.println("Reservation 10: " + response10);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Task10 failed: " + e.getCause().getMessage());
        }


        executor.shutdown();

        List<Reservation> reservations = reservationRepository.findAll();
        long count = reservations.stream()
                .filter(r -> r.getRoom().getId().equals(roomId))
                .filter(r -> !r.getIsDeleted())
                .count();

        System.out.println("동시성 테스트 결과: " + count + "개의 예약 생성됨");

        assertTrue(count == 1, "예약 관련 동시성 문제 테스트 결과");

    }
}