package kl0418;

/**
 * Tool class represents a Tool that user can checkout
 * 
 * @toolCode: sting - ex. 'CHNS' internal tracking code
 * @tootType: string - type of the tool
 * @brand: string - the tool brand
 */
public class Tool {
	private String toolCode;
	private String toolType;
	private String brand;

	/**
	 * Using the Builder pattern to separate the construction of a complex object
	 */
	private Tool(Builder builder) {
		this.toolCode = builder.toolCode;
		this.toolType = builder.toolType;
		this.brand = builder.brand;
	}

	public static class Builder {
		private String toolCode;
		private String toolType;
		private String brand;

		/**
		 * Using the Builder pattern to separate the construction of a complex object,
		 * with the Tool object potentially get lots of variables
		 * @param toolCode - the internal tool code
		 * @param toolType - the internal tool type
		 * @param brand - the tool brand
		 */
		public Builder(String toolCode, String toolType, String brand) {
			this.toolCode = toolCode;
			this.toolType = toolType;
			this.brand = brand;
		}

		public Builder toolCode(String toolCode) {
			this.toolCode = toolCode;
			return this;
		}

		public Builder toolType(String toolType) {
			this.toolType = toolType;
			return this;
		}

		public Builder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public Tool build() {
			return new Tool(this);
		}
	}

	/**
	 * Returns toolCode
	 * @return toolCode
	 */
	public String getToolCode() {
		return this.toolCode;
	}

	/**
	 * Returns toolType
	 * @return toolType
	 */
	public String getToolType() {
		return this.toolType;
	}

	/**
	 * Returns tool's brand
	 * @return tool's brand
	 */
	public String getBrand() {
		return this.brand;
	}

}
