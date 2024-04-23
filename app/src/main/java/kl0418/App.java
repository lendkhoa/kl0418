/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package kl0418;

import java.util.*;

public class App {
    private static Helper helper = new Helper();

    /**
     * Starts the tool rental checkout process
     */
    public static void startCheckout() {
        printInventoryAndPrices();
        UserInput userInput = helper.getUserInputs();

        AgreementGenerator generator = new AgreementGenerator(userInput);
        String agreement = generator.generateAgreement();
        System.out.println(agreement);
    }

    /**
     * Loads the store inventories and charging prices
     */
    private static void printInventoryAndPrices() {
        helper.printInventoryAndPrices();
    }

    public static void main(String[] args) {
        boolean isDebug = Arrays.asList(args).contains("debug");
        String debugFlag = isDebug ? "True" : "False";
        System.setProperty("DEBUG_FLAG", debugFlag);
        startCheckout();
    }
}
