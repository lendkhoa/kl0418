package kl0418;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;

/**
 * File loader to simply print the content from a text file
 */
public class Helper {
	public Helper() {

	}

	/**
	 * Retrieves user inputs for checkout process
	 */
	public void getUserInputs() {
		Scanner scanner = new Scanner(System.in);
		String toolCode = getToolCode(scanner);
		int rentalDayCount = getRentalDayCount(scanner);
		System.out.println("User selected: " + toolCode);
		System.out.println("User selected: " + rentalDayCount);

		scanner.close();
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
						System.out.println(" ⛔️ Invalid tool code. Please enter a valid code.");
						scanner.next();
					} else {
						return rentalDayCount;
					}
				} catch (InputMismatchException e) {
					System.out.println(" ⛔️ Invalid tool code. Please enter a valid code.");
					scanner.next();
				}

			} else {
				System.out.println(" ⛔️ No input found. Please enter a valid rental day count.");
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
