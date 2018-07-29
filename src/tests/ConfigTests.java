package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.Data;
import config.JSONConfig;

public class ConfigTests {

	@Test
	public void test_empty_file() throws FileNotFoundException, IOException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		try {
			JSONConfig j = new JSONConfig("test_files/config/empty.json");
			fail("Expected exception");
		} catch (JSONException e) {
			//pass
		} 
	}
	
	@Test
	public void test_bool_config() throws FileNotFoundException, IOException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		
		JSONConfig j = new JSONConfig("test_files/config/bool.json");
		
		Data expectedBool = j.getTopLevelData().getSubData("bool");
		
		assertEquals(false,expectedBool.getData());
		assertEquals(false,expectedBool.getDeletable());
		assertEquals(false,expectedBool.getExtendable());
		assertEquals(true,expectedBool.getSettable());
		assertEquals("test file", expectedBool.getDeletable());
		
		
	}

}
