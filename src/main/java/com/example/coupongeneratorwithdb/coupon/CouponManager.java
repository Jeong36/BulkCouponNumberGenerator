package com.example.coupongeneratorwithdb.coupon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
public class CouponManager {
    private static final int COUPON_LENGTH = 30; // 쿠폰 번호의 길이
    private static final int NUM_COUPONS = 100000; // 발행할 쿠폰 번호의 개수
    private final Set<String> usedCoupons = new HashSet<>(500000);

    @GetMapping("/coupons")
    public String createCoupon() {
        // DB에서 기존 쿠폰 번호를 로드한다.
        Instant start = Instant.now(); // 메서드 실행 시작 시간 측정
        //Set<String> usedCoupons = CouponDBManager.loadCoupons();

        // 가능한 모든 쿠폰 번호를 생성하고, 중복 검사를 해서 중복되지 않는 쿠폰 번호를 리스트에 저장한다.
        List<String> coupons = new ArrayList<>();
        while (coupons.size() < NUM_COUPONS) {
            String coupon = generateCoupon(COUPON_LENGTH);
            if (!usedCoupons.contains(coupon)) {
                coupons.add(coupon);
                usedCoupons.add(coupon);
            }
        }

        // 생성한 쿠폰 번호를 DB에 저장한다.
        CouponDBManager.saveCoupons(coupons);

        // 실행시간을 측정하고자 하는 코드
        // ...
        Instant end = Instant.now(); // 메서드 실행 종료 시간 측정
        Duration timeElapsed = Duration.between(start, end); // 실행시간 계산

        return "실행 시간: " + timeElapsed.toMillis() + "ms";
    }

    // 지정된 길이의 무작위 쿠폰 번호를 생성한다.
    private String generateCoupon(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }
}

