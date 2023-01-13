package com.password_checker;

public class pass_strength {
    public static void check_pass(String password) throws InvalidPasswordException {
        int upChars = 0, lowChars = 0;
        int special = 0, digits = 0;
        if (password.length() < 8) {
            throw new InvalidPasswordException("Password is too short!! Enter again:");
        } else {
            for (int i = 0; i < password.length(); i++) {
                char ch = password.charAt(i);
                if (Character.isUpperCase(ch)) {
                    upChars = 1;
                } else if (Character.isLowerCase(ch)) {
                    lowChars = 1;
                } else if (Character.isDigit(ch)) {
                    digits = 1;
                } else {
                    special = 1;
                }
            }
        }
        if (upChars == 1 && lowChars == 1 && digits == 1 && special == 1) {
            System.out.println("Strong password");
        } else {
            throw new InvalidPasswordException("Weak Password----Try again!!!");
        }
    }

}
