package com.andrew.eri;

import static org.junit.Assert.*;

import org.junit.Test;
import com.andrew.revpro.Util;

public class UtilTest {
	
	@Test
	public void removeBulletPointTest() {
		String first = Util.removeBulletPoints("• some bullet point");
		String second = Util.removeBulletPoints("● another bullet point");
		assertFalse(first.contains("•"));
		assertFalse(second.contains("●"));
	}

}
