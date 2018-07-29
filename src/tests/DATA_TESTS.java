package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.Data;
import config.ETYPE;
import config.validation.GreaterThan0;

public class DATA_TESTS {

	/**
	 * Test Data object can be initialised with all values specified
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_1() throws ConfigNotValidException, CustomValidationException {
		
		Data d = new Data("name","description",new Integer(4),true, true, true);
		
		assertEquals(d.getDescription(),"description");
		assertEquals(d.getName(),"name");
		assertEquals(d.getType(),ETYPE.INTEGER);
		assertEquals(d.getSettable(),true);
		assertEquals(d.getData(), new Integer(4));
		assertEquals(d.getExtendable(), true);
		assertEquals(d.getDeletable(), true);
	}

	/**
	 * Test Data object can be initialised with all values except settable specified
	 * settable should default to false
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_2() throws ConfigNotValidException, CustomValidationException {
		
		Data d = new Data("name 2","description 2","A string", false, false, false);
		
		assertEquals(d.getDescription(),"description 2");
		assertEquals(d.getName(),"name 2");
		assertEquals(d.getType(),ETYPE.STRING);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), "A string");
		assertEquals(d.getExtendable(), false);
		assertEquals(d.getDeletable(), false);
	}
	
	/**
	 * Test Data object can be initialised with a JSONObject 
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_3() throws ConfigNotValidException, JSONException, InvalidTypeException, CustomValidationException {
		Data d = new Data("name 3","description 3",new Boolean(true), false, true, true);
		
		assertEquals(d.getDescription(),"description 3");
		assertEquals(d.getName(),"name 3");
		assertEquals(d.getType(),ETYPE.BOOLEAN);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), new Boolean(true));
		assertEquals(d.getExtendable(), true);
		assertEquals(d.getDeletable(), true);
		
		
		JSONObject j = d.getJSONOBJ();
		
		Data e = new Data(j);
		
		assertEquals(e.getDescription(),"description 3");
		assertEquals(e.getName(),"name 3");
		assertEquals(e.getType(),ETYPE.BOOLEAN);
		assertEquals(e.getSettable(),false);
		assertEquals(e.getData(), new Boolean(true));
		assertEquals(e.getExtendable(), true);
		assertEquals(e.getDeletable(), true);
	}
	
	/**
	 * Test Data object can be initialised with a JSONObject 
	 * Intialise using a Integer ArrayLIst
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_4() throws ConfigNotValidException, JSONException, InvalidTypeException, CustomValidationException {
		
		ArrayList<Data> list = new ArrayList<Data>();
		for (int i=0;i<10;i++) {
			list.add(new Data("INTEGER " + String.valueOf(i),"INTEGER " + String.valueOf(i),i, true, true, true));
		}
		
		
		Data d = new Data("name 4","description 4",list, false, true, true);
		
		assertEquals(d.getDescription(),"description 4");
		assertEquals(d.getName(),"name 4");
		assertEquals(d.getType(),ETYPE.LIST);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), list);
		assertEquals(d.getExtendable(), true);
		assertEquals(d.getDeletable(), true);
		
		d.setSettable(true);
		
		JSONObject j = d.getJSONOBJ();
		
		Data e = new Data(j);
		
		assertEquals(e.getDescription(),"description 4");
		assertEquals(e.getName(),"name 4");
		assertEquals(e.getType(),ETYPE.LIST);
		assertEquals(e.getSettable(),true);
		assertEquals(e.getData().toString(), list.toString());
		assertEquals(e.getExtendable(), true);
		assertEquals(e.getDeletable(), true);
	}
	

	
	/**
	 * Test Data object can be initialised with a JSONObject 
	 * Intialise using a sub Data object
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_6() throws ConfigNotValidException, JSONException, InvalidTypeException, CustomValidationException {
		
		try {
			new Data("subData","sub description", new StringBuilder(), true, true, true);
			fail("Should not be able to initalise data object using string builder");
		}catch(ConfigNotValidException e) {
			//pass
		}
	}
	
	@Test
	public void data_setting() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		ArrayList<Data> list = new ArrayList<Data>();
		for (int i=0;i<10;i++) {
			list.add(new Data("INTEGER " + String.valueOf(i),"INTEGER " + String.valueOf(i),i, true, true, true));
		}
		
		
		Data d = new Data("name 4","description 4",list, false, true, true);
		
		assertEquals(d.getDescription(),"description 4");
		assertEquals(d.getName(),"name 4");
		assertEquals(d.getType(),ETYPE.LIST);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), list);
		assertEquals(d.getExtendable(), true);
		assertEquals(d.getDeletable(), true);
		
		d.setExtendable(false);
		d.setDeleteable(false);
		d.setName("New Name");
		d.setDescription("New Description");
		
		assertEquals(d.getExtendable(), false);
		assertEquals(d.getDeletable(), false);
		assertEquals(d.getDescription(),"New Description");
		assertEquals(d.getName(),"New Name");
		
		d.setSettable(true);
		
		JSONObject j = d.getJSONOBJ();
		
		Data e = new Data(j);
		
		assertEquals(e.getDescription(),"New Description");
		assertEquals(e.getName(),"New Name");
		assertEquals(e.getType(),ETYPE.LIST);
		assertEquals(e.getSettable(),true);
		assertEquals(e.getData().toString(), list.toString());
		assertEquals(e.getExtendable(), false);
		assertEquals(e.getDeletable(), false);
	}
	
	
	@Test
	public void construct_from_ivalid_json2() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		
		JSONObject j = new JSONObject();
		
		try {
			new Data(j);
			fail("Constructing a Data object off an empty json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("type", "STRING");
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("data", "Data");
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("settable", false);
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("description", "A description");
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		

		j.put("validationObject", JSONObject.NULL);
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("extendable", false);
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("deleteable", false);
		try {
			new Data(j);
			fail("Constructing a Data object off an invalid json objecct should fail");
		}catch (Exception e) {
			//pass
		}
		
		j.put("name", "A name");
		try {
			new Data(j);
			
		}catch (Exception e) {
			fail("Constructing a Data object off an invalid json objecct should fail");
		}
	}
	
	@Test
	public void construct_from_valid_json() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		
		JSONObject j = new JSONObject();
		
		JSONObject v = new JSONObject();
		v.put("name", "GreaterThan0");
		j.put("type", "INTEGER");
		j.put("data", 5);
		j.put("settable", false);
		j.put("description", "a description");
		j.put("validationObject", v);		
		j.put("extendable", false);
		j.put("deleteable", false);		
		j.put("name", "a name");

		Data d = new Data(j);
		

		
		j.put("data", -1);
		
		try {
			new Data(j);
			fail("Expected exception");
		}catch (CustomValidationException e) {
			//pass
		}
		
		j.put("type","DOUBLE");
		j.put("data", 2.4);
		d = new Data(j);
		
		j.put("data", -2.4);
		
		try {
			new Data(j);
			fail("Expected exception");
		}catch (CustomValidationException e) {
			//pass
		}
		
		
		try {
			d.setData("string");
			fail("Expected exception");
		}catch (CustomValidationException e) {
			//pass
		}
	}
	
	@Test
	public void construct_from_valid_json_data_is_object() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		
		JSONObject j = new JSONObject();
		
		Data sub = new Data("name 2","description 2","A string", false, false, false);
		JSONObject subJSON = new JSONObject();
		subJSON.put("sub", sub.getJSONOBJ());

		j.put("type", "OBJECT");
		j.put("data", subJSON);
		j.put("settable", false);
		j.put("description", "a description");
		j.put("validationObject", JSONObject.NULL);		
		j.put("extendable", false);
		j.put("deleteable", false);		
		j.put("name", "a name");

		Data d = new Data(j);
		
	}
	
	@Test
	public void test_data_object() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
	
		JSONObject j = new JSONObject();
		
		Data sub = new Data("name 1","description 2","A string", false, false, false);
		JSONObject subJSON = new JSONObject();
		subJSON.put("sub 1", sub.getJSONOBJ());
		
		Data sub2 = new Data("name 2","description 2","A string", false, false, false);
		subJSON.put("sub 2", sub.getJSONOBJ());
		
		Data sub3 = new Data("name 3","description 2","A string", false, false, false);
		subJSON.put("sub 3", sub.getJSONOBJ());
		
		Data sub4 = new Data("name 4","description 2","A string", false, false, false);
		subJSON.put("sub 4", sub.getJSONOBJ());

		j.put("type", "OBJECT");
		j.put("data", subJSON);
		j.put("settable", false);
		j.put("description", "a description");
		j.put("validationObject", JSONObject.NULL);		
		j.put("extendable", false);
		j.put("deleteable", false);		
		j.put("name", "a name");

		Data d = new Data(j);
		
		JSONObject e = d.getJSONOBJ();
		
		Data d2 = new Data(e);
		
		assertEquals(true, d.equals(d2));
		
		Data sub5 = new Data("name 5","description 2","A string", false, false, false);
		d2.addData(sub5);
		
		assertEquals(true, d2.getSubData("name 5").equals(sub5));
		

		assertEquals(false, d.equals(d2));
		
		d2.removeSubData("name 5");
		assertEquals(true, d.equals(d2));
	}
	
	@Test
	public void test_to_string_and_back_again() throws JSONException, InvalidTypeException, ConfigNotValidException, CustomValidationException {

		Data parent = new Data("parent","parent", new HashMap<String,Data>(),false,false,false);
		Data sub = new Data("list 1","list 1",new ArrayList<Data>(), false, false, false);	
		Data subsub1 = new Data("boolean 1","boolean 1", true, false, false, false);
		Data subsub2 = new Data("boolean 2","boolean 2", true, false, false, false);
		Data subsub3 = new Data("string 1", "string 1", "val", false, false, false);
		Data subsub4 = new Data("string 2", "string 2", "val", false, false, false);
		Data subsub5 = new Data("integer 1","integer 1", Integer.MIN_VALUE, false, false, false);
		Data subsub6 = new Data("integer 2","integer 2", Integer.MAX_VALUE, false, false, false);
		Data subsub7 = new Data("double 1", "double 1", Double.MIN_VALUE, false, false, false);
		Data subsub8 = new Data("double 2", "double 2", Double.MAX_VALUE, false, false, false);
		
		subsub6.setValidationObject(new GreaterThan0());
		
		sub.addData(subsub1);
		sub.addData(subsub2);
		sub.addData(subsub3);
		sub.addData(subsub4);
		sub.addData(subsub5);
		sub.addData(subsub6);
		sub.addData(subsub7);
		sub.addData(subsub8);

		parent.addData(sub);
		
		JSONObject obj = parent.getJSONOBJ();
		String jsonStr = obj.toString();
		
		
		Data convertedData = new Data(new JSONObject(jsonStr));	
		
		assertEquals(true, parent.equals(convertedData));	
	}
	
	@Test
	public void test_setValivationObject() throws ConfigNotValidException {
	
		Data d = new Data("name", "description", "a string", true, true, true);
		
		try {
			d.setValidationObject(new GreaterThan0());
			fail("Exception expected");
		} catch (CustomValidationException e) {
			
		}
		
		try {
			d.setDataAndValidation(-1, new GreaterThan0());
			fail("Exception expected");
		} catch (CustomValidationException e) {
			
		}
		
		try {
			d.setDataAndValidation(1, new GreaterThan0());
		} catch (CustomValidationException e) {
			fail("Exception not expected");
		}
		
		try {
			d.setData("a string");
			fail("Exception expected");
		} catch (CustomValidationException e) {
			
		}
		
		try {
			d.setDataAndValidation("a string", null);
		} catch (CustomValidationException e) {
			fail("Exception not expected");
		}
		
		
	}
}

