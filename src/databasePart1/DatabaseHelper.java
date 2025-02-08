package databasePart1;

import java.io.*;
import java.util.*;
import application.User;

import java.util.ArrayList;

public class DatabaseHelper {
    private Map<String, User> users; // Store users in memory
    private Set<String> invitationCodes;
    private static final String USER_FILE = "users.txt";
    private static final String INVITE_FILE = "invites.txt";

    public DatabaseHelper() {
        users = new HashMap<>();
        invitationCodes = new HashSet<>();
        loadData();
    }

    public void connectToDatabase() {
        // No need for database connection
        loadData();
    }

    private void loadData() {
        // Load users from file
        try {
            File userFile = new File(USER_FILE);
            if (userFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        User user = new User(parts[0], parts[1], "");
                        user.setRolesFromString(parts[2]);
                        users.put(parts[0], user);
                    }
                }
                reader.close();
            }

            // Load invitation codes
            File inviteFile = new File(INVITE_FILE);
            if (inviteFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(inviteFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    invitationCodes.add(line.trim());
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try {
            // Save users
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE));
            for (User user : users.values()) {
                writer.write(String.format("%s,%s,%s%n", 
                    user.getUserName(), 
                    user.getPassword(), 
                    user.getRolesAsString()));
            }
            writer.close();

            // Save invitation codes
            writer = new BufferedWriter(new FileWriter(INVITE_FILE));
            for (String code : invitationCodes) {
                writer.write(code + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(User user) {
        users.put(user.getUserName(), user);
        saveData();
    }

    public boolean login(User user) {
        User storedUser = users.get(user.getUserName());
        if (storedUser != null && storedUser.getPassword().equals(user.getPassword())) {
            user.setRolesFromString(storedUser.getRolesAsString());
            return true;
        }
        return false;
    }

    public ArrayList<String> getUserRoles(String userName) {
        User user = users.get(userName);
        return user != null ? user.getRoles() : new ArrayList<String>();
    }

    public void addUserRole(String userName, String role) {
        User user = users.get(userName);
        if (user != null) {
            user.addRole(role);
            saveData();
        }
    }

    public void removeUserRole(String userName, String role) {
        User user = users.get(userName);
        if (user != null) {
            user.removeRole(role);
            saveData();
        }
    }

    public Set<String> getAllUsers() {
        return new HashSet<>(users.keySet());
    }

    public boolean doesUserExist(String userName) {
        return users.containsKey(userName);
    }

    public boolean isDatabaseEmpty() {
        return users.isEmpty();
    }

    public String generateInvitationCode() {
        String code = UUID.randomUUID().toString().substring(0, 8);
        invitationCodes.add(code);
        saveData();
        return code;
    }

    public boolean validateInvitationCode(String code) {
        return invitationCodes.contains(code);
    }

    public void closeConnection() {
        saveData();
    }
}