package kl0418;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class AgreementGenerator {
	private UserInput userInput;
	private HashMap<String, String[]> info;
	private HashMap<String, Object> policies;

	public AgreementGenerator(UserInput userInput) {
		this.userInput = userInput;
		// NOTE: We will load these content from database in production
		this.info = new HashMap<>();
		this.info.put("CHNS", new String[] { "Chainsaw", "Stihl" });
		this.info.put("LADW", new String[] { "Ladder", "Werner" });
		this.info.put("JAKD", new String[] { "Jackhammer", "DeWalt" });
		this.info.put("JAKR", new String[] { "Jackhammer", "Ridgid" });

		// Tool charging policies
		policies = new HashMap<>();
		// Daily Charge, Weekend Charge, Holiday Charge
		Object[] ladderTool = { 1.99, true, false };
		Object[] chainsawTool = { 1.49, false, true };
		Object[] jackhammerTool = { 2.99, false, false };
		policies.put("CHNS", chainsawTool);
		policies.put("LADW", ladderTool);
		policies.put("JAKD", jackhammerTool);
		policies.put("JAKR", jackhammerTool);
	}

	/**
	 * Generates a rental agreement for the user selected tool
	 */
	public void generateAgreement() {
		int numbersOfChargingDays = calculateChargeableDays();
		double prediscountCharge = calculatePrediscountCharge(numbersOfChargingDays,
				(Object[]) policies.get(userInput.toolCode));

		String agreement = " \n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: " + userInput.toolCode + "\n" +
				"Tool type: " + info.get(userInput.toolCode)[0] + "\n" +
				"Tool brand: " + info.get(userInput.toolCode)[1] + "\n" +
				"Rental days: " + userInput.rentalDayCount + "\n" +
				"Check-out date: " + userInput.checkoutDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")) + "\n" +
				"Due date: " + calculateDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")) + "\n" +
				"Charge days: " + numbersOfChargingDays + "\n" +
				"Pre-discount charge: $" + prediscountCharge + "\n" +
				"=========================";
		System.out.println(agreement);
	}

	private double calculatePrediscountCharge(int days, Object[] policy) {
		double rate = (double) policy[0];
		return roundUpToNextCent(rate * days);
	}

	/**
	 * Returns the rental due date.
	 * 
	 * @return the dueDate
	 */
	private LocalDate calculateDueDate() {
		LocalDate checkoutDate = userInput.checkoutDate;
		LocalDate dueDate = checkoutDate.plusDays(userInput.rentalDayCount);

		return dueDate;
	}

	/**
	 * Calculates chargeable days
	 * 
	 * @return the dueDate
	 */
	private int calculateChargeableDays() {
		int chargeableDays = 0;
		LocalDate startDate = userInput.checkoutDate;
		LocalDate dueDate = startDate.plusDays(userInput.rentalDayCount);

		Object[] policy = (Object[]) policies.get(userInput.toolCode);

		// Start counting chargeable day from day after checkout date
		startDate = startDate.plusDays(1);
		// Calculate total rental days
		while (!startDate.isAfter(dueDate)) {
			chargeableDays += 1;
			// Subtract for non chargeable days
			chargeableDays -= nonChargeableDays(startDate, policy);
			startDate = startDate.plusDays(1);
		}

		return chargeableDays;
	}

	private int nonChargeableDays(LocalDate date, Object[] toolPolicy) {
		int nonChargeableDay = 0;
		boolean chargeOnWeekend = (boolean) toolPolicy[1];
		boolean chargeOnHoliday = (boolean) toolPolicy[2];
		System.out.println("Checking " + date.toString());
		if (isWeekend(date) && !chargeOnWeekend) {
			System.out.println("|__is weekend and not charge on weekend");
			// no weekend charge
			nonChargeableDay += 1;
		} else if (isHoliday(date) && !chargeOnHoliday) {
			System.out.println("|__is holiday and not charge on holidays");
			// no holiday charge
			nonChargeableDay += 1;
		} else if (isWeekend(date) && isIndependenceDay(date)) {
			System.out.println("|__is weekend and independence day");
			// independence day on Sat -> observe Friday
			// tool A doesn't charge on Holiday +1 but charge on weekend -1
			if (!chargeOnHoliday) {
				System.out.println("|____ not charge on holiday");
				nonChargeableDay += 1;
			}
			if (!chargeOnWeekend) {
				System.out.println("|____ not charge on weekend");
				nonChargeableDay += 1;
			}
			if (chargeOnHoliday) {
				System.out.println("|____ charge on holiday");
				nonChargeableDay -= 1;
			}
			if (chargeOnWeekend) {
				System.out.println("|____ charge on weekend");
				nonChargeableDay -= 1;
			}
		}

		return nonChargeableDay;
	}

	/**
	 * Rounds a double value up to the nearest cent.
	 *
	 * @param value the double value to round
	 * @return the rounded value, rounded up to the nearest cent
	 */
	public static double roundUpToNextCent(double value) {
		return Math.ceil(value * 100) / 100.0;
	}

	/**
	 * Returns true if local date is Independence day or Labor day
	 * 
	 * @param date the local date
	 * @return true if local date is Independence day or Labor day, false otherwise
	 */
	private boolean isHoliday(LocalDate date) {
		return isIndependenceDay(date) || isLaborDay(date);
	}

	/**
	 * Returns true if LocalDate is Labor Day (first Monday of September)
	 * 
	 * @param date the local date to check
	 * @return true if LocalDate is Labor Day, false otherwise
	 */
	private static boolean isLaborDay(LocalDate date) {
		int year = date.getYear(); // Get the year of the date
		LocalDate laborDay = LocalDate.of(year, 9, 1); // First day of September in the same year
		while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY) {
			laborDay = laborDay.plusDays(1); // Find the first Monday of September
		}
		return date.equals(laborDay); // Check if the date matches Labor Day
	}

	/**
	 * Returns true if local date is Independence day
	 * 
	 * @param date the local date
	 * @return true if local date is Independence day, false otherwise
	 */
	private static boolean isIndependenceDay(LocalDate date) {
		return date.getMonthValue() == 7 && date.getDayOfMonth() == 4;
	}

	/**
	 * Returns true if local date is weekend
	 * 
	 * @param date the local date
	 * @return true if local date is weekend
	 */
	private static boolean isWeekend(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	}
}
