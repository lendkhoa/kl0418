import java.util.*;
import java.util.Scanner;

public class UserInputExample {

    private static void test() {
        Scanner scanner = new Scanner(System.in);

        Set<String> validSet = new HashSet<>(Arrays.asList("CHNS", "LADW", "JAKD", "JAKR"));
        while (true) {
            System.out.print("Enter tool code: ");
            if (scanner.hasNextLine()) {
                String toolCode = scanner.nextLine().toUpperCase();
                System.out.println("Checking " + toolCode);
                if (validSet.contains(toolCode)) {
                    System.out.println(toolCode);
                    break;
                } else {
                    System.out.println("Invalid tool code. Please enter a valid code.");
                }
            } else {
                System.out.println("No input found. Please enter a valid code.");
                scanner.next(); // Consume the newline character
            }
        }
    }

    public static void main(String[] args) {
        test();
    }
}
