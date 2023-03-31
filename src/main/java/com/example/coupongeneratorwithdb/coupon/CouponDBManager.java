package com.example.coupongeneratorwithdb.coupon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CouponDBManager {

    private static final int BATCH_SIZE = 1000; // 쿠폰 번호를 나누어서 조회할 때 한 번에 조회할 개수
    private static final String DB_URL = "jdbc:h2:mem:testdb?MODE=ORACLE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void saveCoupons(List<String> coupons) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO coupons (coupon_code) VALUES (?)")) {
            conn.setAutoCommit(false); // 자동 커밋을 방지하여 속도를 높인다.
            for (String coupon : coupons) {
                stmt.setString(1, coupon);
                stmt.addBatch(); // 쿼리를 배치에 추가한다.
            }
            stmt.executeBatch(); // 배치에 추가한 모든 쿼리를 실행한다.
            conn.commit(); // 모든 쿼리가 성공적으로 실행되면 커밋한다.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> loadCoupons() {
        Set<String> couponSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.setFetchSize(BATCH_SIZE); // 쿠폰 번호를 나누어서 조회한다.
            ResultSet rs = stmt.executeQuery("SELECT coupon_code FROM coupons");
            while (rs.next()) {
                String coupon = rs.getString("coupon_code");
                couponSet.add(coupon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return couponSet;
    }
}
