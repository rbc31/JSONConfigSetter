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
import config.validation.GreaterThan0;
import config.validation.IntegerList;

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
		assertEquals("test file", expectedBool.getDescription());
		
		expectedBool.setData(1);
		expectedBool.setName("number");
		expectedBool.setDeleteable(true);
		expectedBool.setExtendable(true);
		expectedBool.setSettable(false);
		expectedBool.setDescription("test file 2");
		
		j.save("test_files/config/integer.json");
		
		j = new JSONConfig("test_files/config/integer.json");
		
		Data expectedInt = j.getTopLevelData().getSubData("number");
		
		assertEquals(1,expectedInt.getData());
		assertEquals(true,expectedInt.getDeletable());
		assertEquals(true,expectedInt.getExtendable());
		assertEquals(false,expectedInt.getSettable());
		assertEquals("test file 2", expectedInt.getDescription());
		
		assertTrue(j.validate());
		
		j.addValidation(new IntegerList());
		
		assertTrue(j.validate());
		
		expectedInt.setData(true);
		
		assertFalse(j.validate());
		
	}
	
	@Test
	public void test_create_new_config() throws FileNotFoundException, IOException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
	
		
		JSONConfig j1 = new JSONConfig();
		
		Data j1_data = j1.getTopLevelData();
		
		j1_data.addData(new Data("String var","String var","String var",true,true,true));
		
		assertTrue( null ==j1.getFilePath());
		
		
		j1.save("test_files/config/string.json");
		
		JSONConfig j2 = new JSONConfig("test_files/config/string.json");
		
		assertTrue(j1_data.equals(j2.getTopLevelData()));
		
		j1_data.setData(true);
		
		
		j1.save("test_files/config/string.json");
		
		assertEquals("test_files/config/string.json",j1.getFilePath());
		
		j2.reload();
		
		assertTrue(j1_data.equals(j2.getTopLevelData()));
		assertEquals(true, j2.getTopLevelData().getData());
		
	}

}
