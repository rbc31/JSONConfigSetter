package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.Data;
import config.ETYPE;

public class DATA_TESTS {

	/**
	 * Test Data object can be initialised with all values specified
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_1() throws ConfigNotValidException, CustomValidationException {
		
		Data d = new Data("name","description",new Integer(4),true);
		
		assertEquals(d.getDescription(),"description");
		assertEquals(d.getName(),"name");
		assertEquals(d.getType(),ETYPE.INTEGER);
		assertEquals(d.getSettable(),true);
		assertEquals(d.getData(), new Integer(4));
	}

	/**
	 * Test Data object can be initialised with all values except settable specified
	 * settable should default to false
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_2() throws ConfigNotValidException, CustomValidationException {
		
		Data d = new Data("name 2","description 2","A string");
		
		assertEquals(d.getDescription(),"description 2");
		assertEquals(d.getName(),"name 2");
		assertEquals(d.getType(),ETYPE.STRING);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), "A string");
	}
	
	/**
	 * Test Data object can be initialised with a JSONObject 
	 * @throws ConfigNotValidException
	 * @throws CustomValidationException 
	 */
	@Test
	public void data_initialisation_test_3() throws ConfigNotValidException, JSONException, InvalidTypeException, CustomValidationException {
		Data d = new Data("name 3","description 3",new Boolean(true));
		
		assertEquals(d.getDescription(),"description 3");
		assertEquals(d.getName(),"name 3");
		assertEquals(d.getType(),ETYPE.BOOLEAN);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), new Boolean(true));
		
		JSONObject j = d.getJSONOBJ();
		
		Data e = new Data(j);
		
		assertEquals(e.getDescription(),"description 3");
		assertEquals(e.getName(),"name 3");
		assertEquals(e.getType(),ETYPE.BOOLEAN);
		assertEquals(e.getSettable(),false);
		assertEquals(e.getData(), new Boolean(true));
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
			list.add(new Data("INTEGER " + String.valueOf(i),"INTEGER " + String.valueOf(i),i ));
		}
		
		
		Data d = new Data("name 4","description 4",list);
		
		assertEquals(d.getDescription(),"description 4");
		assertEquals(d.getName(),"name 4");
		assertEquals(d.getType(),ETYPE.LIST);
		assertEquals(d.getSettable(),false);
		assertEquals(d.getData(), list);
		
		d.setSettable(true);
		
		JSONObject j = d.getJSONOBJ();
		
		Data e = new Data(j);
		
		assertEquals(e.getDescription(),"description 4");
		assertEquals(e.getName(),"name 4");
		assertEquals(e.getType(),ETYPE.LIST);
		assertEquals(e.getSettable(),true);
		assertEquals(e.getData().toString(), list.toString());
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
			new Data("subData","sub description", new StringBuilder(), true);
			fail("Should not be able to initalise data object using string builder");
		}catch(ConfigNotValidException e) {
			//pass
		}
	}
}
