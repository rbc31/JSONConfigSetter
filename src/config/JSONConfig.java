package config;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.*;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.validation.ValidationObject;

public class JSONConfig {

	private JSONObject obj = null;
	private Data data;
	private String filePath;
	private ValidationObject validationObject;
	
	public JSONConfig() {
		this.obj = new JSONObject();
		initalizeEmptyData();
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	private String loadFile(String filePath) throws  FileNotFoundException, IOException {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			
			String temp = br.readLine();
			StringBuilder toReturn = new StringBuilder();
			
			while (temp != null) {
				toReturn.append(temp);
				temp = br.readLine();
			}
			return toReturn.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
	
	private void initalizeEmptyData() {
		try {
			this.data = new Data("Data", "Top level data object", new ArrayList<Data>(), true, true, true);
		} catch (ConfigNotValidException e) {
			//never called
		} 
	}
	
	public JSONConfig(String filePath) throws FileNotFoundException, JSONException, IOException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		this.filePath = filePath;
		this.obj = new JSONObject(loadFile(filePath));
		initalizeEmptyData();

		this.data = new Data( this.obj.getJSONObject("Data"));

	}
	
	private void reloadObjBasedOffData() {
		this.obj = null;
		
		this.obj = new JSONObject();
		this.obj.put("Data", this.data.getJSONOBJ());	
	}
	
	public void save(String filePath) throws FileNotFoundException {
		reloadObjBasedOffData();
		PrintWriter pr = new PrintWriter(filePath);
		pr.write(this.obj.toString());
		pr.close();
		this.filePath = filePath;
	}
	
	public void reload() throws JSONException, FileNotFoundException, IOException, InvalidTypeException, ConfigNotValidException, CustomValidationException {
		this.obj = new JSONObject(loadFile(this.filePath));
		this.data = new Data( this.obj.getJSONObject("Data"));
	}
	
	public void addValidation(ValidationObject validationObject) {
		this.validationObject = validationObject;
	}
	
	public boolean validate() {
		return this.validationObject == null || validationObject.valid(this.data.getData());
	}
	
	public Data getTopLevelData() {
		return this.data;
	}
}
