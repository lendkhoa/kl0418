package kl0418;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Generate a rental agreement
 */
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
	 * 
	 * @return agreement the string agreement
	 */
	public String generateAgreement() {
		int numbersOfChargingDays = calculateChargeableDays();
		double prediscountCharge = calculatePrediscountCharge(numbersOfChargingDays,
				(Object[]) policies.get(userInput.toolCode));
		int discountPercent = userInput.discountPercent;
		double discountAmount = roundUpToNextCent(discountPercent * prediscountCharge / 100.0);
		double finalCharge = roundUpToNextCent(prediscountCharge - discountAmount);

		String agreement = " \n\n === RENTAL AGREEMENT === \n\n" +
				"Tool code: " + userInput.toolCode + "\n" +
				"Tool type: " + info.get(userInput.toolCode)[0] + "\n" +
				"Tool brand: " + info.get(userInput.toolCode)[1] + "\n" +
				"Rental days: " + userInput.rentalDayCount + "\n" +
				"Check-out date: " + userInput.checkoutDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")) + "\n" +
				"Due date: " + calculateDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")) + "\n" +
				"Charge days: " + numbersOfChargingDays + "\n" +
				"Pre-discount charge: $" + prediscountCharge + "\n" +
				"Discount percent: " + discountPercent + "%\n" +
				"Discount amount: $" + discountAmount + "\n" +
				"Final charge: $" + finalCharge + "\n" +
				"\n==========================";
		return agreement;
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
		boolean chargeOnWeekend = (boolean) policy[1];
		boolean chargeOnHoliday = (boolean) policy[2];

		// Start counting chargeable day from day after checkout date
		LocalDate today = startDate.plusDays(1);
		// Calculate total rental days
		while (!today.isAfter(dueDate)) {
			Helper.printDebug("🐛 Checking " + today);
			// Today is a regular weekday
			if (!isWeekend(today) && !isHoliday(today)) {
				Helper.printDebug("Charge today " + today + " because it's a regular weekday.");
				chargeableDays += 1;
				today = today.plusDays(1);
				Helper.printDebug("Chargeable days " + chargeableDays);
				continue;
			}
			// Today is either weekend or holiday (Independence day or Labor day)

			// Case 1: Policy - Charge on weekend, charge on holiday
			// No effect to the nonChargeableDay because we calculate the
			// rental fee on the weekend day and holiday.
			// Notice that we don't want to charge the user twice on the holiday's observed
			// day.
			// Because the logic prioritize user's experience and loyalty over only day
			// extra income
			if (chargeOnWeekend && chargeOnHoliday) {
				Helper.printDebug("This tool charges on both weekend and holiday");
				chargeableDays += 1;
				today = today.plusDays(1);
				Helper.printDebug("Charge today " + today
						+ " because the tool charges on both weekend and holiday. Chargeable days " + chargeableDays);
			} else if (chargeOnWeekend && !chargeOnHoliday) {
				Helper.printDebug("This tool charges weekend but not on holidays");
				// Case 2: Policy - Charge on weekend, no charge on holiday
				// No charge on the holiday's observed day (Either Friday or Monday)
				// Notice that the observed day must be in the range [checkoutDate + 1,
				// DueDate].
				// If the observed day is outside this range then we don't apply the no charge
				if (isWeekend(today) && !isHoliday(today)) {
					chargeableDays += 1;
					today = today.plusDays(1);
					Helper.printDebug(
							"Charge today " + today + " because the tool charges on weekend. Chargeable days " + chargeableDays);
				} else if (isWeekend(today) && isIndependenceDay(today)) {
					DayOfWeek dayOfWeek = today.getDayOfWeek();
					if (dayOfWeek == DayOfWeek.SATURDAY) {
						// The independence day is on Saturday
						LocalDate observedFriday = today.minusDays(1);
						if (observedFriday.isAfter(startDate)
								&& (observedFriday.isBefore(dueDate) || observedFriday.isEqual(dueDate))) {
							chargeableDays -= 1;
							today = today.plusDays(1);
							Helper.printDebug("No charge on Independence day observed Friday " + observedFriday
									+ " because the tool doesn't charge on holiday, independence day on weekend. Chargeable days "
									+ chargeableDays);
						}
					} else {
						// The independence day is on Sunday
						LocalDate observedMonday = today.plusDays(1);
						if (observedMonday.isAfter(startDate)
								&& (observedMonday.isBefore(dueDate) || observedMonday.isEqual(dueDate))) {
							chargeableDays -= 1;
							// We don't want to calculate the observed monday in the range
							today = today.plusDays(2);
							Helper.printDebug("No charge on Independence day observed Monday " + observedMonday
									+ " because the tool doesn't charge on holiday, independence day on weekend. Chargeable days "
									+ chargeableDays);
						}
					}
				} else {
					// Today is Labor day
					Helper.printDebug("No charge on Labor day (Monday) " + today);
					// We don't want to calculate the observed monday in the range
					today = today.plusDays(1);
				}

			} else if (!chargeOnWeekend && chargeOnHoliday) {
				Helper.printDebug("This tool doesn't charge on weekend but charge on holidays");
				// Case 3: Policy - No charge on weekend, charge on holiday
				// We don't charge the fee on the weekend, but we charge the fee once on
				// the holiday observed day the observed day must be in the range [checkoutDate
				// + 1, DueDate].
				if (isWeekend(today) && !isHoliday(today)) {
					Helper.printDebug(
							"No charge " + today + " because the tool doesn't charge on weekend");
					today = today.plusDays(1);
					Helper.printDebug("Chargeable days: " + chargeableDays);
				} else if (isWeekend(today) && isIndependenceDay(today)) {
					// If the holiday is on weekend, we, again, favor the lower fee for customer
					// and don't charge the today (as a holiday) but also don't take double charge
					// the observed day
					DayOfWeek dayOfWeek = today.getDayOfWeek();
					if (dayOfWeek == DayOfWeek.SATURDAY) {
						LocalDate observedFriday = today.minusDays(1);
						if (observedFriday.isAfter(startDate)
								&& (observedFriday.isBefore(dueDate) || observedFriday.isEqual(dueDate))) {
							Helper.printDebug("No charge " + today
									+ " because the tool doesn't charge on weekend. \n The observed Friday is in the rental range, it was already charged.");
							today = today.plusDays(1);
							Helper.printDebug("Chargeable days: " + chargeableDays);
						}
					} else {
						// The independence day is on Sunday. We don't charge on Sunday but charge
						// the observed Monday once.
						LocalDate observedMonday = today.plusDays(1);
						if (observedMonday.isAfter(startDate)
								&& (observedMonday.isBefore(dueDate) || observedMonday.isEqual(dueDate))) {
							Helper.printDebug("Charge today " + today + " independence day on Sunday");
							chargeableDays += 1;
							// We don't want to double-charged the observed monday in the range. Start
							// checking Tuesday
							today = today.plusDays(2);
							Helper.printDebug("No charge on Independence day observed Monday " + observedMonday
									+ " because the tool doesn't charge on holiday, independence day on weekend. Chargeable days: "
									+ chargeableDays);
						}
					}
				} else {
					// Today is Labor day. The tool charges on Labor day
					Helper.printDebug("Charge today " + today
							+ " Labor day because the tool charges on holiday.");
					chargeableDays += 1;
					today = today.plusDays(1);
					Helper.printDebug("Chargeable days: " + chargeableDays);
				}
			} else if (!chargeOnWeekend && !chargeOnHoliday) {
				Helper.printDebug("This tool doesn't charge on both weekend and holidays");
				// Case 4: Policy - No charge on weekend, no charge on holiday
				// day the observed day must be in the range [checkoutDate + 1, DueDate]
				if (isWeekend(today) && !isHoliday(today)) {
					Helper.printDebug("No charge today " + today);
					today = today.plusDays(1);
				} else if (isWeekend(today) && isIndependenceDay(today)) {
					DayOfWeek dayOfWeek = today.getDayOfWeek();
					if (dayOfWeek == DayOfWeek.SATURDAY) {
						// The independence day is on Saturday
						LocalDate observedFriday = today.minusDays(1);
						if (observedFriday.isAfter(startDate)
								&& (observedFriday.isBefore(dueDate) || observedFriday.isEqual(dueDate))) {
							chargeableDays -= 1;
							today = today.plusDays(1);
							Helper.printDebug("No charge on Independence day observed Friday " + observedFriday
									+ " because the tool doesn't charge on holiday, independence day on weekend. Chargeable days: "
									+ chargeableDays);
						}
					} else {
						// The independence day is on Sunday
						LocalDate observedMonday = today.plusDays(1);
						if (observedMonday.isAfter(startDate)
								&& (observedMonday.isBefore(dueDate) || observedMonday.isEqual(dueDate))) {
							chargeableDays -= 1;
							// We don't want to calculate the observed monday in the range
							today = today.plusDays(2);
							Helper.printDebug("No charge on Independence day observed Monday " + observedMonday
									+ " because the tool doesn't charge on holiday, independence day on weekend. Chargeable days: "
									+ chargeableDays);
						}
					}
				} else {
					// Today is Labor day. Don't charge today.
					Helper.printDebug("No charge today " + today + " labor day");
					today = today.plusDays(1);
				}
			}

		}

		return chargeableDays;
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
