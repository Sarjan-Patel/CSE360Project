package application;

public class PasswordEvaluator {

    public static String evaluatePassword(String input) {
        StringBuilder errors = new StringBuilder(); // Collects multiple errors

        if (input.isEmpty()) {
            errors.append("Password cannot be empty.\n");
        }

        if (input.length() < 8) {
            errors.append("Password must be at least 8 characters long.\n");
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        // Allowed special characters
        String specialCharacters = "@#$%&*!?+=^~";

        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecialChar = true;
            } else if (Character.isWhitespace(c)) {
                errors.append("Password cannot contain spaces.\n");
            }
        }

        if (!hasUpperCase) {
            errors.append("Password must include at least one uppercase letter (A-Z).\n");
        }

        if (!hasLowerCase) {
            errors.append("Password must include at least one lowercase letter (a-z).\n");
        }

        if (!hasNumber) {
            errors.append("Password must include at least one number (0-9).\n");
        }

        if (!hasSpecialChar) {
            errors.append("Password must include at least one special character (@#$%&*!?+=^~).\n");
        }

        return errors.toString().trim(); // Return all errors together
    }
}
