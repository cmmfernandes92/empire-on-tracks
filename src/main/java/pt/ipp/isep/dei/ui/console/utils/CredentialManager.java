package pt.ipp.isep.dei.ui.console.utils;

import java.util.UUID;
import java.util.regex.Pattern;

public class CredentialManager {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=(?:.*[A-Z]){3,})(?=(?:.*\\d){2,})[A-Za-z\\d]{7}$"
    );

    public static String generateUserId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
}
