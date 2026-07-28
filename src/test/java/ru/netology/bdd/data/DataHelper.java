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

    @Value
    public static class CardInfo {
        String id;
        String number;
        int balance;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo(
                "92df3f1c-a033-48e6-8390-206f6b1f56c0",
                "5559 0000 0000 0001",
                10000
        );
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo(
                "0f3f5c2a-249e-4c3d-8287-09f7a039391d",
                "5559 0000 0000 0002",
                10000
        );
    }
}