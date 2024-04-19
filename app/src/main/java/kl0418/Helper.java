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
	public Helper() {

	}

	/**
	 * Retrieves user inputs for checkout process
	 */
	public UserInput getUserInputs() {
		Scanner scanner = new Scanner(System.in);

		String toolCode = getToolCode(scanner);
		int rentalDayCount = getRentalDayCount(scanner);
		int discountPercent = getDiscountPercent(scanner);
		LocalDate checkoutDate = getCheckoutDate(scanner);
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
		Set<String> validSet = new HashSet<>(Arrays.asList("CHNS", "LADW", "JAKD", "JAKR"));
		while (true) {
			System.out.print("Enter tool code: ");
			if (scanner.hasNextLine()) {
				String toolCode = scanner.nextLine().toUpperCase();
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
	 * Retrieves the user input for rental day count. Rejects if the input is
	 * greater than 365 days.
	 * 
	 * @param scanner the system in scanner
	 * @return the valid user selected rental day count
	 */
	public int getRentalDayCount(Scanner scanner) {
		while (true) {
			System.out.print("Enter rental day count: ");
			if (scanner.hasNextLine()) {
				try {
					int rentalDayCount = scanner.nextInt();
					if (rentalDayCount > 365) {
						System.out.println(" ⛔️ Invalid rental day count");
						scanner.next();
					} else {
						return rentalDayCount;
					}
				} catch (InputMismatchException e) {
					System.out.println(" ⛔️ Invalid rental day count");
					scanner.next();
				}

			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid rental day count.");
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
		while (true) {
			System.out.print("Enter discount percent (0-100): ");
			if (scanner.hasNextLine()) {
				try {
					int discountPercent = scanner.nextInt();
					if (discountPercent < 0 || discountPercent > 100) {
						System.out.println(" ⛔️ Discount percent must be < 100");
						scanner.next();
					} else {
						return discountPercent;
					}
				} catch (InputMismatchException e) {
					System.out.println(" ⛔️ Invalid discount percent. Please enter a valid discount percent.");
					scanner.next();
				}

			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid discount percent.");
				scanner.next();
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		System.out.print("Enter checkout date (MM/dd/yyyy): ");
		while (true) {
			if (scanner.hasNextLine()) {
				try {
					String checkoutDateStr = scanner.nextLine();
					if (!checkoutDateStr.isBlank()) {
						LocalDate date = LocalDate.parse(checkoutDateStr, formatter);
						return date;
					}
				} catch (DateTimeParseException e) {
					System.out.println(" ⛔️ Invalid check-out date. Please enter a valid date.");
					System.out.print("Enter checkout date (MM/dd/yyyy): ");
					scanner.next();
				}
			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid discount percent.");
				System.out.print("Enter checkout date (MM/dd/yyyy): ");
				scanner.next();
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

}
