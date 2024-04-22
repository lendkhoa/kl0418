package kl0418;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class HelperTest {
	private final InputStream systemIn = System.in;
	private ByteArrayInputStream testIn;

	/**
	 * Tests the getToolCodeValidInput with valid input
	 * Assert the expected tool code returned
	 */
	@Test
	public void testGetToolCodeValidInput() {
		testIn = new ByteArrayInputStream("CHNS\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		String toolCode = helper.getToolCode(scanner);
		assertEquals("CHNS", toolCode);

		System.setIn(systemIn);
	}

	/**
	 * Tests the getRentalDayCountValidInput method with valid input
	 * Assert the expected dental day count returned
	 */
	@Test
	public void testGetRentalDayCountValidInput() {
		testIn = new ByteArrayInputStream("10\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		int toolCode = helper.getRentalDayCount(scanner);
		assertEquals(10, toolCode);

		System.setIn(systemIn);
	}

	/**
	 * Tests the getRentalDayCount method with invalid input.
	 * 
	 * This test simulates user input of "-10" (a string) instead of a valid
	 * integer. The function should return -1
	 */
	@Test
	public void testGetRentalDayCount_LessThanZero() {
		testIn = new ByteArrayInputStream("-10\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		int toolCode = helper.getRentalDayCount(scanner);
		assertEquals(-1, toolCode);

		System.setIn(systemIn);
	}

	/**
	 * Tests the getRentalDayCount method with invalid input.
	 * 
	 * This test simulates user input of "2000" (a string representing a long time) instead of a valid
	 * integer. The function should return -1
	 */
	@Test
	public void testGetRentalDayCount_MaxValue() {
		testIn = new ByteArrayInputStream("2000\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		int toolCode = helper.getRentalDayCount(scanner);
		assertEquals(-1, toolCode);

		System.setIn(systemIn);
	}

	/**
	 * Tests the getDiscountPercentValidInput method with valid input
	 * Assert the expected discount percent returned
	 */
	@Test
	public void testGetDiscountPercentValidInput() {
		testIn = new ByteArrayInputStream("10\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		int discountPercent = helper.getDiscountPercent(scanner);
		assertEquals(10, discountPercent);

		System.setIn(systemIn);
	}

	/**
	 * Tests the getDiscountPercent method with invalid input.
	 * 
	 * This test simulates user input of "-10" (a string) instead of a valid
	 * integer. The function should return -1
	 */
	@Test
	public void testGetDiscountPercent_LessThanZero() {
		// Set up the input
		byte[] input = "-10\n".getBytes();
		ByteArrayInputStream testIn = new ByteArrayInputStream(input);
		System.setIn(testIn);

		// Create a Scanner object
		Scanner scanner = new Scanner(System.in);

		// Call the method
		int result = new Helper().getDiscountPercent(scanner);

		assertEquals(-1, result);
		System.setIn(System.in);
	}

}
