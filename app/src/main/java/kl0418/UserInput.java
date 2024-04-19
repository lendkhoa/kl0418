package kl0418;

import java.time.LocalDate;

public class UserInput {
	public String toolCode;
	public int rentalDayCount;
	public int discountPercent;
	public LocalDate checkoutDate;

	/**
	 * Using the Builder pattern to separate the construction of a complex object
	 */
	private UserInput(Builder builder) {
		this.toolCode = builder.toolCode;
		this.rentalDayCount = builder.rentalDayCount;
		this.discountPercent = builder.discountPercent;
		this.checkoutDate = builder.checkoutDate;
	}

	public static class Builder {
		private String toolCode;
		private int rentalDayCount;
		private int discountPercent;
		private LocalDate checkoutDate;

		/**
		 * Using the Builder pattern to separate the construction of a complex object,
		 * with the Tool object potentially get lots of variables
		 * 
		 * @param toolCode - the internal tool code
		 * @param toolType - the internal tool type
		 * @param brand    - the tool brand
		 */
		public Builder() {
		}

		public Builder toolCode(String toolCode) {
			this.toolCode = toolCode;
			return this;
		}

		public Builder rentalDayCount(int rentalDayCount) {
			this.rentalDayCount = rentalDayCount;
			return this;
		}

		public Builder discountPercent(int discountPercent) {
			this.discountPercent = discountPercent;
			return this;
		}

		public Builder checkoutDate(LocalDate checkoutDate) {
			this.checkoutDate = checkoutDate;
			return this;
		}

		public UserInput build() {
			return new UserInput(this);
		}
	}
}
