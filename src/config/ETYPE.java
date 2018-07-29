package config;



import java.util.ArrayList;

import Exceptions.InvalidTypeException;

public enum ETYPE {

	STRING, INTEGER, BOOLEAN, DOUBLE, OBJECT, LIST;

	public static ETYPE getTypeOfObject(Object obj) {
		if 		(obj instanceof String) 			return STRING;
		else if (obj instanceof Integer)    			return INTEGER;
		else if (obj instanceof Boolean)    			return BOOLEAN;
		else if (obj instanceof Double)     			return DOUBLE;
		else if (obj instanceof ArrayList) 				return LIST;
		else 											return OBJECT;
	}
	
	public String toString() {
		if 		(this.equals(STRING)) 			return "STRING";
		else if (this.equals(INTEGER))     		return "INTEGER";
		else if (this.equals(BOOLEAN))     		return "BOOLEAN";
		else if (this.equals(DOUBLE))    		return "DOUBLE";
		else if (this.equals(LIST))     		return "LIST";
		else 									return "OBJECT";
	}
	
	public static ETYPE fromString(String name) throws InvalidTypeException {
		if 		(name.equals("STRING")) 		return STRING;
		else if (name.equals("INTEGER"))     	return INTEGER;
		else if (name.equals("BOOLEAN"))     	return BOOLEAN;
		else if (name.equals("DOUBLE"))    		return DOUBLE;
		else if (name.equals("LIST"))   		return LIST;
		else if (name.equals("OBJECT"))			return OBJECT;
		throw new InvalidTypeException("Name " + name + " is not recognised as a valid type");
	}

}