package config;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Exceptions.ConfigNotValidException;
import Exceptions.CustomValidationException;
import Exceptions.InvalidTypeException;
import config.validation.GreaterThan0;
import config.validation.ValidationObject;

public class Data {

	private ETYPE 	type;
	private Object 	data;
	private boolean settable;
	private String 	description;
	private String 	name;
	private ValidationObject validationObject;
	private boolean extendable;
	private boolean deleteable;

	public Data(String name, String description, Object data, boolean settable, boolean extendable, boolean deleteable) throws ConfigNotValidException, CustomValidationException {
		this.name = name;
		this.settable = settable;
		this.description = description;
		this.setData(data);
		this.validationObject = null;
		this.extendable = extendable;
		this.deleteable = deleteable;
	}
	
	public Data(JSONObject jsonObject) throws JSONException, InvalidTypeException, ConfigNotValidException {
		if (	jsonObject.has("type") 				&&
				jsonObject.has("data") 				&&
				jsonObject.has("settable")	 		&&
				jsonObject.has("description") 		&&
				jsonObject.has("validationObject") 	&&
				jsonObject.has("extendable") 		&&
				jsonObject.has("deleteable") 		&&
				jsonObject.has("name")) {
			
			this.name 			= jsonObject.getString("name");
			this.description 	= jsonObject.getString("description");
			this.settable 		= jsonObject.getBoolean("settable");
			this.type 			= ETYPE.fromString(jsonObject.getString("type"));
			this.extendable     = jsonObject.getBoolean("extendable");
			this.deleteable     = jsonObject.getBoolean("deleteable");
			
			if (jsonObject.isNull("validationObject")) {
				this.validationObject = null;
			}else {
				this.validationObject = loadValidationObject(jsonObject.getJSONObject("validationObject"));
			}
			
				switch (this.type) {
					case LIST:
						ArrayList<Data> arr = new ArrayList<Data>();
						JSONArray temp = jsonObject.getJSONArray("data");
						
						for (int i=0;i<temp.length();i++) {
							arr.add(new Data(temp.getJSONObject(i)));
						}
						this.data = arr;
						break;
						
					case OBJECT: 
						HashMap<String,Data> map = new HashMap<String,Data>();
						JSONObject tempObj = jsonObject.getJSONObject("data");
						
						for (String name : tempObj.keySet()) {
							map.put(name,new Data(tempObj.getJSONObject(name)));
						}
						this.data = map;
						break;
					case INTEGER:
						this.data 			= jsonObject.getInt("data");
						break;
					case STRING:
						this.data 			= jsonObject.getString("data");
						break;
					case DOUBLE:
						this.data 			= jsonObject.getDouble("data");
						break;
					case BOOLEAN:
						this.data 			= jsonObject.getBoolean("data");
						break;
				}

		}else {
			throw new ConfigNotValidException("Data object must have: type, data, settable, description, validationObject, extendable, deleteable and name keys defined");
		}
	}
	
	public ETYPE getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public boolean getSettable() {
		return this.settable;
	}
	
	public boolean getDeletable() {
		return this.deleteable;
	}
	
	public boolean getExtendable() {
		return this.extendable;
	}
	
	public void setDeletable(boolean deleteable) {
		this.deleteable = deleteable;
	}
	
	public void setExtendable(boolean extendable) {
		this.extendable = extendable;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setSettable(boolean settable) {
		this.settable = settable;
	}
	
	public void setDataAndValidation(Object data, ValidationObject v) throws ConfigNotValidException, CustomValidationException {
		this.validationObject = v;
		this.setData(data);
	}
	
	public void setData(Object data) throws ConfigNotValidException, CustomValidationException {
		if (!this.valid(data)) {
			throw new CustomValidationException("Data is not valid!");
		}
		
		ETYPE type = ETYPE.getTypeOfObject(data);

		if (type == ETYPE.OBJECT) {
			if (data instanceof HashMap<?,?>) {
				HashMap<?,?> map = (HashMap<?,?>) data;
				for (Object key : map.keySet()) {
					if (!(map.get(key) instanceof Data)) {
						throw new ConfigNotValidException("A sub object of a Data object must be another Data object!");
					}
				}
			} else {
				throw new ConfigNotValidException("A sub object of a Data object must be another Data object!");
			}
		}
		
		if (type == ETYPE.LIST) {
			if (data instanceof ArrayList<?>) {
				ArrayList<?> list = (ArrayList<?>) data;
				for (int i=0;i < list.size();i++) {
					if (!(list.get(i) instanceof Data)) {
						throw new ConfigNotValidException("A sub object of a Data object must be another Data object!");
					}
				}
			} else {
				throw new ConfigNotValidException("A sub object of a Data object must be another Data object!");
			}
		}
		
		this.data = data;
		this.type = ETYPE.getTypeOfObject(data);
		
	}
	
	private boolean valid() {
		return this.validationObject == null || this.validationObject.valid(this.data);
	}
	
	private boolean valid(Object data) {
		return this.validationObject == null || this.validationObject.valid(data);
	}
	
	public void setValidationObject(ValidationObject v) throws CustomValidationException {
		this.validationObject = v;
		if (!this.valid()) {
			throw new CustomValidationException("Data is not valid!");
		}
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder("Data Object");
		
		s.append(" Name: " 			+ this.name);
		s.append(" Description = " 	+ this.description);
		s.append(" Type = " 		+ this.type);
		s.append(" isSettable = " 	+ this.settable);
		s.append(" isExtendable = " 	+ this.extendable);
		s.append(" isDeleteable = " 	+ this.deleteable);
		s.append(" Value = " 		+ this.data.toString());
		
		return s.toString();
	}
	
	public JSONObject getJSONOBJ() {
		JSONObject toReturn = new JSONObject();
		
		toReturn.put("type", this.type.toString());
		toReturn.put("settable", this.settable);
		toReturn.put("description", this.description);
		toReturn.put("name", this.name);
		toReturn.put("extendable",this.extendable);
		toReturn.put("deleteable",this.deleteable);
		
		if (this.validationObject == null) {
			toReturn.put("validationObject", JSONObject.NULL);
		}else {
			toReturn.put("validationObject", this.validationObject.toJSONOBJ());
		}
		
		if (this.type == ETYPE.OBJECT) {
			JSONObject obj = new JSONObject();
			HashMap<?,?> map = (HashMap<?,?>) this.data;
			
			for (Object key: map.keySet()) {
				obj.put((String) key, ((Data)map.get(key)).getJSONOBJ());
			}
			toReturn.put("data", obj);
		}else if (this.type == ETYPE.LIST) {
			JSONArray arr = new JSONArray();
			ArrayList<?> list = (ArrayList<?>) this.data;
			
			for (int i=0;i<list.size();i++) {
				arr.put(i, ((Data) list.get(i)).getJSONOBJ());
			}
			toReturn.put("data", arr);
		}else {
			toReturn.put("data", this.data);
		}
		
		return toReturn;
	}
	
	public ValidationObject loadValidationObject(JSONObject obj) throws JSONException, ConfigNotValidException {
		if (obj.has("name")) {
			switch (obj.getString("name")) {
			
			case "GreaterThan0":
				return new GreaterThan0();
			default:
				throw new ConfigNotValidException("Validation name " + obj.getString("name") + " is not valid");
			}
		}
		throw new ConfigNotValidException("Data object doesn't have a validation object!");
		
	}
	
	public void addData(Data dataToAdd) throws ConfigNotValidException {
		if (this.type == ETYPE.LIST) {
			((ArrayList) this.data).add(dataToAdd);
		}else if (this.type == ETYPE.OBJECT) {
			HashMap map = (HashMap<?,?>) this.data;
			
			if (map.get(dataToAdd.getName()) == null) {
				map.put(dataToAdd.getName(),dataToAdd);
			}else {
				throw new ConfigNotValidException("That Value already exists.");
			}
			
		}else {
			throw new ConfigNotValidException("Can only add sub data to list or object");
		}
	}

	public void removeSubData(String name) {
		// TODO Auto-generated method stub
		if (this.type == ETYPE.OBJECT) {
			HashMap map = (HashMap<?,?>)this.data;
			
			if (map.get(name) == null) {
				throw new IllegalArgumentException("Name " + name + " is not a member of this object");
			}else {
				map.remove(name);
			}
		}else if (this.type == ETYPE.LIST) {
			ArrayList<?> list = (ArrayList<?>) this.data;
			
			for (Object element: list) {
				if (((Data) element).getName().equals(name)) {
					list.remove(element);
					return;
				}
			}
			throw new IllegalArgumentException("Name " + name + " is not a member of this list");
		}
	}
	
	
	
	
	
	
}
