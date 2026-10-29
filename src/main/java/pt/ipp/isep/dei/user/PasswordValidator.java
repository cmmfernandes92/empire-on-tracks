package pt.ipp.isep.dei.user;

/**
 * Validates password complexity rules such as length, uppercase letters, and digits.
 */
public class PasswordValidator {

    /**
     * Checks if the provided password meets security criteria:
     * minimum length, required uppercase letters and digits.
     *
     * @param password the password string to validate
     * @return true if valid, false otherwise
     */
    public boolean validate(String password) {
        if (password == null) return false;
        if (password.length() < 7) return false;

        int uppercaseCount = 0;
        int digitCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) uppercaseCount++;
            if (Character.isDigit(c)) digitCount++;
        }

        return uppercaseCount >= 3 && digitCount >= 2;
    }
}
