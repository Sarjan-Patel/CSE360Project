package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 * Supported roles: admin, student, instructor, staff, and reviewer.
 */
public class User {
    private String userName;
    private String password;
    private String role;
    
    // Define valid roles as constants
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_INSTRUCTOR = "instructor";
    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_REVIEWER = "reviewer";
    
    // Store valid roles in an array for validation
    private static final String[] VALID_ROLES = {
        ROLE_ADMIN,
        ROLE_STUDENT,
        ROLE_INSTRUCTOR,
        ROLE_STAFF,
        ROLE_REVIEWER
    };

    /**
     * Constructor to initialize a new User object with userName, password, and role.
     * @param userName The user's username
     * @param password The user's password
     * @param role The user's role (must be one of the valid roles)
     * @throws IllegalArgumentException if the role is invalid
     */
    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        setRole(role); // Use setRole to validate the role
    }

    /**
     * Sets the role of the user.
     * @param role The role to set
     * @throws IllegalArgumentException if the role is invalid
     */
    public void setRole(String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role: " + role + 
                ". Role must be one of: admin, student, instructor, staff, or reviewer");
        }
        this.role = role.toLowerCase(); // Store roles in lowercase for consistency
    }

    /**
     * Checks if a given role is valid.
     * @param role The role to check
     * @return true if the role is valid, false otherwise
     */
    public static boolean isValidRole(String role) {
        if (role == null) return false;
        String lowercaseRole = role.toLowerCase();
        for (String validRole : VALID_ROLES) {
            if (validRole.equals(lowercaseRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all valid roles in the system.
     * @return Array of valid roles
     */
    public static String[] getValidRoles() {
        return VALID_ROLES.clone(); // Return a copy to prevent modification
    }

    /**
     * Checks if the user is an admin.
     * @return true if the user has admin role, false otherwise
     */
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }

    // Getters for user properties
    public String getUserName() { 
        return userName; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getRole() { 
        return role; 
    }

    /**
     * Returns a string representation of the User object.
     * Note: Password is excluded for security reasons.
     */
    @Override
    public String toString() {
        return "User{" +
            "userName='" + userName + '\'' +
            ", role='" + role + '\'' +
            '}';
    }
}