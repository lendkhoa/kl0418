package kl0418;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class HelperTest {
	private final InputStream systemIn = System.in;
	private ByteArrayInputStream testIn;

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

	@Test
	public void testGetRentalDayCountValidInput() {
		testIn = new ByteArrayInputStream("10\n".getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		Helper helper = new Helper();
		int toolCode = helper.getRentalDayCount(scanner);
		assertEquals("CHNS", toolCode);

		System.setIn(systemIn);
	}
}
