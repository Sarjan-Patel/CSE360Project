package application;

import java.util.List;
import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private List<String> roles;

    public User(String userName, String password, String initialRole) {
        this.userName = userName;
        this.password = password;
        this.roles = new ArrayList<String>();
        if (initialRole != null && !initialRole.isEmpty()) {
            addRole(initialRole);
        }
    }
    
    public boolean addRole(String role) {
        if (isValidRole(role)) {
            roles.add(role.toLowerCase());
            return true;
        }
        return false;
    }
    
    public void removeRole(String role) {
        roles.remove(role.toLowerCase());
    }
    
    public boolean hasRole(String role) {
        return roles.contains(role.toLowerCase());
    }
    
    public ArrayList<String> getRoles() {
        return new ArrayList<String>(roles);
    }
    
    private boolean isValidRole(String role) {
        if (role == null) return false;
        String normalizedRole = role.toLowerCase();
        return normalizedRole.equals("admin") ||
               normalizedRole.equals("student") ||
               normalizedRole.equals("instructor") ||
               normalizedRole.equals("staff") ||
               normalizedRole.equals("reviewer");
    }

    public String getRolesAsString() {
        return String.join(",", roles);
    }
    
    public void setRolesFromString(String rolesStr) {
        roles.clear();
        if (rolesStr != null && !rolesStr.isEmpty()) {
            String[] roleArray = rolesStr.split(",");
            for (String role : roleArray) {
                if (isValidRole(role.trim())) {
                    roles.add(role.trim().toLowerCase());
                }
            }
        }
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
}