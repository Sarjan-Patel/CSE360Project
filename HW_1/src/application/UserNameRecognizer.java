package application;

public class UserNameRecognizer {

    public static String checkForValidUserName(String input) {
        StringBuilder errors = new StringBuilder(); // Collects multiple errors

        if (input.isEmpty()) {
            errors.append("Username cannot be empty.\n");
        }

        if (input.length() < 4) {
            errors.append("Username must have at least 4 characters.\n");
        }

        if (input.length() > 16) {
            errors.append("Username cannot exceed 16 characters.\n");
        }

        // First character must be a letter (A-Z, a-z) and NOT a special character
        char firstChar = input.charAt(0);
        if (!Character.isLetter(firstChar)) {
            errors.append("Username must start with a letter (A-Z or a-z) and not a special character.\n");
        }

        // Allowed characters: A-Z, a-z, 0-9, '_', '-', '.'
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.')) {
                errors.append("Username can only contain letters, numbers, '_', '-', or '.'.\n");
                break; // Only add this message once
            }
        }

        // Last character cannot be a special character
        char lastChar = input.charAt(input.length() - 1);
        if (lastChar == '_' || lastChar == '-' || lastChar == '.') {
            errors.append("Username cannot end with '_', '-', or '.'.\n");
        }

        return errors.toString().trim(); // Return all errors together
    }
}
