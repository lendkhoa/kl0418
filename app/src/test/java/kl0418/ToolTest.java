package kl0418;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToolTest {
	@Test
	public void createToolTest() {
		Tool hammer = new Tool.Builder("CHNS", "Chainsaw", "Stihl").build();
		assertNotNull(hammer);
		assertEquals("CHNS", hammer.getToolCode());
		assertEquals("Chainsaw", hammer.getToolType());
		assertEquals("Stihl", hammer.getBrand());
	}

	@Test
	public void createToolTestNoToolType() {
		Tool hammer = new Tool.Builder("CHNS", null, "Stihl").build();
		assertNotNull(hammer);
		assertNull(hammer.getToolType());
		assertEquals("CHNS", hammer.getToolCode());
		assertEquals("Stihl", hammer.getBrand());
	}
}
