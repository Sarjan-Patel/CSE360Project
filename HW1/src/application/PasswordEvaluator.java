package application;
public class PasswordEvaluator {
	/**
	 * <p> Title: Directed Graph-translated Password Assessor. </p>
	 *
	 * <p> Description: A demonstration of the mechanical translation of Directed Graph
	 * diagram into an executable Java program using the Password Evaluator Directed Graph.
	 * The code detailed design is based on a while loop with a cascade of if statements</p>
	 *
	 * <p> Copyright: Lynn Robert Carter © 2022 </p>
	 *
	 * @author Lynn Robert Carter
	 *
	 * @version 0.00		2018-02-22	Initial baseline
	 *
	 */
	/**********************************************************************************************
	 *
	 * Result attributes to be used for GUI applications where a detailed error message and a
	 * pointer to the character of the error will enhance the user experience.
	 *
	 */
	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;	
	private static int state = 0; 						//FSM current state
	private static int charCounter = 0;					// counting the character
	// The flag that specifies if the FSM is running
	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to
	 * display the error message on the console terminal.
	 *
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	public static void FSMState() {								//defining the state of the FSM instead of the displayInputState method
		System.out.println("State: " + state + "CurrentChar: " + currentChar + ", CharCounter: " + charCounter);		//printing the state and current char
		}
	/**********
	 * This method is a mechanical transformation of a Directed Graph diagram into a Java
	 * method.
	 *
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a help description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		charCounter = 0;					//also sends the character counter to 0
		
		if(input.length() <= 0) return "*** Error *** The password is empty!";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position
		// The Directed Graph simulation continues until the end of the input is reached or at some
		// state the current character does not match any valid transition to a next state
		currentChar = input.charAt(0);
		passwordInput = input;				// Save a copy of the input
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundLongEnough = false;			// Reset the Boolean flag
		running = true;						// Start the loop
		state = 0;							//defining where the state would start 
		// The Directed Graph simulation continues until the end of the input is reached or at some
		// state the current character does not match any valid transition
		while (running) {
			FSMState();
			// The cascading if statement sequentially tries the current character against all of the
			// valid transitions
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
				state = 1;						//state 1
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
				state = 2;						//state 2
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
				state = 3;						//state 3
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
				state = 4;						//state 4
			} else {
				passwordIndexofError = currentCharNdx;
				return "*** Error *** An invalid character has been found!";
			}
			
			charCounter++;					//incrementing the character count 
			
			if (charCounter >= 8) {			//specifying the need for the 8 character password requirement 
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length()) {
				running = false;
			} else {
				currentChar = input.charAt(currentCharNdx);
			
			}
			System.out.println();
		}
		
		String errMessage = "";
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (errMessage == "")
			return "";
		
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";
	}
}

