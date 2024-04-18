package kl0418;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * File loader to simply print the content from a text file
 */
public class FileLoader {
	public FileLoader() {

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
