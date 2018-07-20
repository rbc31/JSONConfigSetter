package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Exceptions.InvalidTypeException;
import config.ETYPE;

public class ETYPE_TESTS {

	
	@Test
	public void test_getTypeOfObject() {
		
		ETYPE type = ETYPE.getTypeOfObject(new Integer (4));
		assertEquals(type,ETYPE.INTEGER);

		type = ETYPE.getTypeOfObject("Hello");
		assertEquals(type,ETYPE.STRING);
		
		type = ETYPE.getTypeOfObject(new Double (4.0));
		assertEquals(type,ETYPE.DOUBLE);
		
		type = ETYPE.getTypeOfObject(new Boolean (true));
		assertEquals(type,ETYPE.BOOLEAN);
		
		type = ETYPE.getTypeOfObject(new StringBuilder(4));
		assertEquals(type,ETYPE.OBJECT);
		
		type = ETYPE.getTypeOfObject(new ArrayList<StringBuilder>(4));
		assertEquals(type,ETYPE.LIST);
	}
	
	@Test
	public void test_ETYPEtoString() {
		ETYPE type;
		
		type = ETYPE.STRING;
		assertEquals(type.toString(),"STRING");
		
		type = ETYPE.INTEGER;
		assertEquals(type.toString(),"INTEGER");
		
		type = ETYPE.DOUBLE;
		assertEquals(type.toString(),"DOUBLE");
		
		type = ETYPE.BOOLEAN;
		assertEquals(type.toString(),"BOOLEAN");
		
		type = ETYPE.LIST;
		assertEquals(type.toString(),"LIST");
		
		type = ETYPE.OBJECT;
		assertEquals(type.toString(),"OBJECT");
	}
	
	@Test
	public void test_ETYPEfromString() {
		ETYPE type;
		try {
			type = ETYPE.fromString("STRING");
			assertEquals(type,ETYPE.STRING);
			
			type = ETYPE.fromString("INTEGER");
			assertEquals(type,ETYPE.INTEGER);
			
			type = ETYPE.fromString("DOUBLE");
			assertEquals(type,ETYPE.DOUBLE);
			
			type = ETYPE.fromString("BOOLEAN");
			assertEquals(type,ETYPE.BOOLEAN);
			
			type = ETYPE.fromString("LIST");
			assertEquals(type,ETYPE.LIST);
			
			type = ETYPE.fromString("OBJECT");
			assertEquals(type,ETYPE.OBJECT);
		} catch (InvalidTypeException e) {
			fail("Encountered exception whilst trying to covert valid types");
		}
		
		try {
			type = ETYPE.fromString("");
			fail("Did not encounter exception whilst trying to covert in valid types");
		} catch (InvalidTypeException e) {
			//pass
		}
		
		try {
			type = ETYPE.fromString("Invalid object");
			fail("Did not encounter exception whilst trying to covert in valid types");
		} catch (InvalidTypeException e) {
			//pass
		}
	}

}
