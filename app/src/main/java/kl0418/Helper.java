package kl0418;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;

/**
 * This is a Helper class that performs various tasks such as loading content
 * from file
 * and getting user's input from terminal
 */
public class Helper {
	private static String DEBUG_FLAG;

	public Helper() {
		DEBUG_FLAG = System.getProperty("DEBUG_FLAG");
	}

	/**
	 * Retrieves user inputs for checkout process
	 */
	public UserInput getUserInputs() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("\n CHECKOUT \n");
		String toolCode = getToolCode(scanner);
		LocalDate checkoutDate = getCheckoutDate(scanner);
		int rentalDayCount = getRentalDayCount(scanner);
		int discountPercent = getDiscountPercent(scanner);
		UserInput userInput = new UserInput.Builder().toolCode(toolCode).rentalDayCount(rentalDayCount)
				.discountPercent(discountPercent).checkoutDate(checkoutDate).build();

		scanner.close();
		return userInput;
	}

	/**
	 * Retrieves the user input for toolcode. Rejects if the input is invalid.
	 * 
	 * @param scanner the system in scanner
	 * @return the valid user selected toolCode
	 */
	public String getToolCode(Scanner scanner) {
		String toolCode = "";
		Set<String> validSet = new HashSet<>(Arrays.asList("CHNS", "LADW", "JAKD", "JAKR"));
		while (true) {
			System.out.print("Enter tool code: ");
			if (!toolCode.isEmpty() || scanner.hasNextLine()) {
				toolCode = scanner.nextLine().toUpperCase();
				if (validSet.contains(toolCode)) {
					return toolCode;
				} else {
					System.out.println(" ⛔️ Invalid tool code. Please enter a valid tool code.");
				}
			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid tool code.");
				scanner.next();
			}
		}
	}

	/**
	 * Retrieves the user input for rental day count. Must be 1 or greater
	 * and smaller than a default MAX_DAYS
	 * 
	 * @param scanner the system in scanner
	 * @return the valid user selected rental day count
	 */
	public int getRentalDayCount(Scanner scanner) {
		int MAX_DAYS = 1095; // 3 years
		System.out.print("Enter rental day count: ");
		while (true) {
			if (scanner.hasNextLine()) {
				try {
					int rentalDayCount = scanner.nextInt();
					if (rentalDayCount < 0 || rentalDayCount > MAX_DAYS) {
						System.out.println(" ⛔️ Rental day count must be 1 or greater and less than " + MAX_DAYS);
						System.out.print("Enter rental day count: ");
						scanner.next();
					} else {
						return rentalDayCount;
					}
				} catch (InputMismatchException e) {
					System.out.println(" ⛔️ Rental day count must be 1 or greater and less than " + MAX_DAYS);
					System.out.print("Enter rental day count: ");
					scanner.next();
				}

			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid rental day count.");
				System.out.print("Enter rental day count: ");
				scanner.next();
			}
		}
	}

	/**
	 * Retrieves the user input for discount percent (whole number). Rejects if the
	 * input is
	 * greater than negative, greater than 100 or not a whole number.
	 * 
	 * @param scanner the system in scanner
	 * @return the valid user selected discount percent
	 */
	public int getDiscountPercent(Scanner scanner) {
		System.out.print("Enter discount percent (0-100): ");
		while (true) {
			if (scanner.hasNextLine()) {
				try {
					int discountPercent = Integer.parseInt(scanner.next().trim());
					if (discountPercent < 0 || discountPercent > 100) {
						System.out.println(" ⛔️ Discount percent must be >= 0 and <= 100");
						System.out.print("Enter discount percent (0-100): ");
					} else {
						return discountPercent;
					}
				} catch (Exception e) {
					System.out.println(" ⛔️ Invalid discount percent. Please enter a valid discount percent.");
					System.out.print("Enter discount percent (0-100): ");
				}

			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid discount percent.");
				System.out.print("Enter discount percent (0-100): ");
			}
		}
	}

	/**
	 * Retrieves the user input for checkout date.
	 * 
	 * @param scanner the system in scanner
	 * @return the valid user selected checkout date
	 */
	public LocalDate getCheckoutDate(Scanner scanner) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
		System.out.print("Enter checkout date (M/d/yy) example 1/1/11: ");
		String checkoutDateStr = "";
		while (true) {
			if (!checkoutDateStr.isEmpty() || scanner.hasNextLine()) {
				try {
					checkoutDateStr = scanner.nextLine();
					if (!checkoutDateStr.isBlank()) {
						LocalDate date = LocalDate.parse(checkoutDateStr, formatter);
						return date;
					}
				} catch (DateTimeParseException e) {
					System.out.println(" ⛔️ Invalid check-out date. Please enter a valid date. " + e.toString());
					System.out.print("Enter checkout date (M/d/yy): ");
					checkoutDateStr = "";
				}
			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid date.");
				System.out.print("Enter checkout date (M/d/yy): ");
				checkoutDateStr = "";
			}
		}
	}

	/*
	 * Prints store inventory and prices
	 */
	public void printInventoryAndPrices() {
		String filePath = "src/main/resources/store_inventories.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}
	}

	/**
	 * Prints a debug message to the console if the DEBUG_FLAG is set to true.
	 *
	 * @param message the debug message to print
	 */
	public static void printDebug(String message) {
		if (DEBUG_FLAG == "True" && !message.isEmpty()) {
			System.out.print(" 🐛: " + message);
		}
	}

}
