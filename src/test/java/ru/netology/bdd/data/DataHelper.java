package ru.netology.bdd.data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    // ID карт (из data-test-id)
    public static String getFirstCardId() {
        return "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    }

    public static String getSecondCardId() {
        return "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
    }

    // Номера карт
    public static String getFirstCardNumber() {
        return "5559 0000 0000 0001";
    }

    public static String getSecondCardNumber() {
        return "5559 0000 0000 0002";
    }
}