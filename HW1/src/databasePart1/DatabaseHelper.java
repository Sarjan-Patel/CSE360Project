package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import application.User;
import java.time.LocalDateTime; 
import java.sql.PreparedStatement;
import java.sql.Timestamp;  
/**
* The DatabaseHelper class is responsible for managing the connection to the database,
* performing operations such as user registration, login validation, and handling invitation codes.
*/
public class DatabaseHelper {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.h2.Driver";  
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase"; 
	//  Database credentials
	static final String USER = "sa";
	static final String PASS = "";
	private Connection connection = null;
	private Statement statement = null;
	//	PreparedStatement pstmt
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement();
			// You can use this command to clear the database and restart from fresh.
			// statement.execute("DROP ALL OBJECTS");
			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}
	private void createTables() throws SQLException {
	    String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
	            + "id INT AUTO_INCREMENT PRIMARY KEY, "
	            + "userName VARCHAR(255) UNIQUE, "
	            + "password VARCHAR(255), "
	            + "role VARCHAR(20), "     // Added comma
	            + "otp VARCHAR(10))";      // Moved closing parenthesis here
	    statement.execute(userTable);
	   

	    // Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "\"ROLE\" VARCHAR(50), "        
	            + "expiration_date TIMESTAMP, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);

	    
	    String checkAdminQuery = "SELECT COUNT(*) AS count FROM cse360users WHERE userName = 'rianachacha'";
	    ResultSet resultSet = statement.executeQuery(checkAdminQuery);
	    resultSet.next();
	   
	    if (resultSet.getInt("count") == 0) {
	        String insertAdmin = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
	        try (PreparedStatement pstmt = connection.prepareStatement(insertAdmin)) {
	            pstmt.setString(1, "rianachacha");
	            pstmt.setString(2, "Chatterjee1_");
	            pstmt.setString(3, "admin");
	            pstmt.executeUpdate();
	            System.out.println("Admin account created: Username - rianachacha");
	        }
	    }
	}
	public void printAllUsers() {
	    try {
	        ResultSet rs = getAllUsers();
	        System.out.println("Current users in database:");
	        while (rs.next()) {
	            System.out.println("Username: " + rs.getString("userName") + ", Role: " + rs.getString("role"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	public ResultSet getAllUsers() throws SQLException {
	    if (connection == null || statement == null) {
	        throw new SQLException("Database connection is not initialized. Call connectToDatabase() first.");
	    }
	    String query = "SELECT userName, role FROM cse360users"; // Only fetch necessary columns
	    return statement.executeQuery(query);
	}
	// Registers a new user in the database.
	public void register(User user) throws SQLException {
	    String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	        pstmt.setString(1, user.getUserName());
	        pstmt.setString(2, user.getPassword());
	        pstmt.setString(3, user.getRole());
	        pstmt.executeUpdate();
	    }
	}
	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	       
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	       
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	       
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String role) {
	    String code = UUID.randomUUID().toString().substring(0, 8); // Longer, more unique code
	    
	    // Set expiration to 7 days from now
	    Timestamp expirationDate = Timestamp.valueOf(
	        LocalDateTime.now().plusDays(7)
	    );
	    
	    String query = "INSERT INTO InvitationCodes (code, \"ROLE\", expiration_date, isUsed) VALUES (?, ?, ?, false)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, role);
	        pstmt.setTimestamp(3, expirationDate);
	        pstmt.executeUpdate();

	        System.out.println("Generated Invitation Code:");
	        System.out.println("Code: " + code);
	        System.out.println("Role: " + role);
	        System.out.println("Expiration: " + expirationDate);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error generating invitation code: " + e.getMessage());
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public String validateInvitationCode(String code) {
		String query = "SELECT \"ROLE\", expiration_date, isUsed FROM InvitationCodes WHERE code = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (!rs.next()) {
	                System.out.println("❌ Invitation code not found.");
	                return null; // Code does not exist
	            }

	            boolean isUsed = rs.getBoolean("isUsed");
	            Timestamp expirationDate = rs.getTimestamp("expiration_date");


	            System.out.println("📜 Invitation Code Details:");
	            System.out.println("🔹 Code: " + code);
	            System.out.println("🔹 Is Used: " + isUsed);
	            System.out.println("🔹 Expiration Date: " + expirationDate);
	            System.out.println("🔹 Current Time: " + Timestamp.valueOf(LocalDateTime.now()));

	            // ✅ Check if the code is already used
	            if (isUsed) {
	                System.out.println("❌ This invitation code has already been used.");
	                return null;
	            }

	            // ✅ Check if the code has expired
	            if (expirationDate.before(Timestamp.valueOf(LocalDateTime.now()))) {
	                System.out.println("❌ This invitation code has expired.");
	                return null;
	            }

	            // ✅ Only return the role, DO NOT mark as used yet (do this after successful registration)
	            return rs.getString("ROLE");  // Changed from "role" to "ROLE"
	        }
	    } catch (SQLException e) {
	        System.err.println("⚠️ Database error during invitation code validation: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return null;
	}

	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	// Closes the database connection and statement.
	public void closeConnection() {
		try{
			if(statement!=null) statement.close();
		} catch(SQLException se2) {
			se2.printStackTrace();
		}
		try {
			if(connection!=null) connection.close();
		} catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	// Set a one-time password for a user
	public void setOneTimePassword(String userName, String otp) throws SQLException {
	    String query = "UPDATE cse360users SET otp = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, otp);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}
	// Check if a user has an OTP set
	public boolean hasOneTimePassword(String userName) throws SQLException {
	    String query = "SELECT otp FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        return rs.next() && rs.getString("otp") != null;
	    }
	}
	// Validate login with OTP
	public boolean isOneTimePassword(String userName, String password) throws SQLException {
	    String query = "SELECT otp FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            String storedOtp = rs.getString("otp");
	            return storedOtp != null && storedOtp.equals(password);
	        }
	    }
	    return false;
	}
	// Clear OTP after user resets password
	public void clearOneTimePassword(String userName) throws SQLException {
	    String query = "UPDATE cse360users SET otp = NULL WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.executeUpdate();
	    }
	}
	// Update user's password and clear OTP
	public void updatePassword(String userName, String newPassword) throws SQLException {
	    String query = "UPDATE cse360users SET password = ?, otp = NULL WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}
}
