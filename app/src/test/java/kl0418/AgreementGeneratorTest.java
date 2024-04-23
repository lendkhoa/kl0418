package kl0418;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class AgreementGeneratorTest {
	/**
	 * Generate the expected agreement for Test 2 with the following user input
	 * Tool code: LADW
	 * Checkout date: 7/2/20
	 * Rental days: 3
	 * Discount: 10
	 */
	@Test
	public void testGenerateAgreement2() {
		LocalDate checkoutDate = LocalDate.of(2020, 07, 02);
		UserInput userInput = new UserInput.Builder().toolCode("LADW").rentalDayCount(3)
				.discountPercent(10).checkoutDate(checkoutDate).build();

		AgreementGenerator generator = new AgreementGenerator(userInput);
		String agreement = generator.generateAgreement();

		// Verify the output
		String expectedOutput = "\n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: LADW\n" +
				"Tool type: Ladder\n" +
				"Tool brand: Werner\n" +
				"Rental days: 3\n" +
				"Check-out date: 07/02/20\n" +
				"Due date: 07/05/20\n" +
				"Charge days: 1\n" +
				"Pre-discount charge: $1.99\n" +
				"Discount percent: 10%\n" +
				"Discount amount: $0.2\n" +
				"Final charge: $1.79\n" +
				"\n==========================";

		assertEquals(expectedOutput.trim(), agreement.trim());
	}

	/**
	 * Generate the expected agreement for Test 2 with the following user input
	 * Tool code: CHNS
	 * Checkout date: 7/2/15
	 * Rental days: 5
	 * Discount: 25
	 */
	@Test
	public void testGenerateAgreement3() {
		LocalDate checkoutDate = LocalDate.of(2015, 07, 02);
		UserInput userInput = new UserInput.Builder().toolCode("CHNS").rentalDayCount(5)
				.discountPercent(25).checkoutDate(checkoutDate).build();

		AgreementGenerator generator = new AgreementGenerator(userInput);
		String agreement = generator.generateAgreement();

		// Verify the output
		String expectedOutput = "\n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: CHNS\n" +
				"Tool type: Chainsaw\n" +
				"Tool brand: Stihl\n" +
				"Rental days: 5\n" +
				"Check-out date: 07/02/15\n" +
				"Due date: 07/07/15\n" +
				"Charge days: 3\n" +
				"Pre-discount charge: $4.47\n" +
				"Discount percent: 25%\n" +
				"Discount amount: $1.12\n" +
				"Final charge: $3.35\n" +
				"\n==========================";

		assertEquals(expectedOutput.trim(), agreement.trim());
	}

	/**
	 * Generate the expected agreement for Test 2 with the following user input
	 * Tool code: JAKD
	 * Checkout date: 9/3/15
	 * Rental days: 6
	 * Discount: 0
	 */
	@Test
	public void testGenerateAgreement4() {
		LocalDate checkoutDate = LocalDate.of(2015, 9, 03);
		UserInput userInput = new UserInput.Builder().toolCode("JAKD").rentalDayCount(6)
				.discountPercent(0).checkoutDate(checkoutDate).build();

		AgreementGenerator generator = new AgreementGenerator(userInput);
		String agreement = generator.generateAgreement();

		// Verify the output
		String expectedOutput = "\n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: JAKD\n" +
				"Tool type: Jackhammer\n" +
				"Tool brand: DeWalt\n" +
				"Rental days: 6\n" +
				"Check-out date: 09/03/15\n" +
				"Due date: 09/09/15\n" +
				"Charge days: 3\n" +
				"Pre-discount charge: $8.98\n" +
				"Discount percent: 0%\n" +
				"Discount amount: $0.0\n" +
				"Final charge: $8.98\n" +
				"\n==========================";

		assertEquals(expectedOutput.trim(), agreement.trim());
	}

	/**
	 * Generate the expected agreement for Test 2 with the following user input
	 * Tool code: JAKR
	 * Checkout date: 7/2/15
	 * Rental days: 9
	 * Discount: 0
	 */
	@Test
	public void testGenerateAgreement5() {
		LocalDate checkoutDate = LocalDate.of(2015, 7, 02);
		UserInput userInput = new UserInput.Builder().toolCode("JAKR").rentalDayCount(9)
				.discountPercent(0).checkoutDate(checkoutDate).build();

		AgreementGenerator generator = new AgreementGenerator(userInput);
		String agreement = generator.generateAgreement();

		// Verify the output
		String expectedOutput = "\n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: JAKR\n" +
				"Tool type: Jackhammer\n" +
				"Tool brand: Ridgid\n" +
				"Rental days: 9\n" +
				"Check-out date: 07/02/15\n" +
				"Due date: 07/11/15\n" +
				"Charge days: 5\n" +
				"Pre-discount charge: $14.95\n" +
				"Discount percent: 0%\n" +
				"Discount amount: $0.0\n" +
				"Final charge: $14.95\n" +
				"\n==========================";

		assertEquals(expectedOutput.trim(), agreement.trim());
	}

	/**
	 * Generate the expected agreement for Test 2 with the following user input
	 * Tool code: JAKR
	 * Checkout date: 7/2/20
	 * Rental days: 4
	 * Discount: 50
	 */
	@Test
	public void testGenerateAgreement6() {
		LocalDate checkoutDate = LocalDate.of(2020, 7, 02);
		UserInput userInput = new UserInput.Builder().toolCode("JAKR").rentalDayCount(4)
				.discountPercent(50).checkoutDate(checkoutDate).build();

		AgreementGenerator generator = new AgreementGenerator(userInput);
		String agreement = generator.generateAgreement();

		// Verify the output
		String expectedOutput = "\n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: JAKR\n" +
				"Tool type: Jackhammer\n" +
				"Tool brand: Ridgid\n" +
				"Rental days: 4\n" +
				"Check-out date: 07/02/20\n" +
				"Due date: 07/06/20\n" +
				"Charge days: 1\n" +
				"Pre-discount charge: $2.99\n" +
				"Discount percent: 50%\n" +
				"Discount amount: $1.5\n" +
				"Final charge: $1.5\n" +
				"\n==========================";

		assertEquals(expectedOutput.trim(), agreement.trim());
	}
}
