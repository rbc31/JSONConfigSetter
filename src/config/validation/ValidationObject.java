package config.validation;

import org.json.JSONObject;

public interface ValidationObject {

	
	/**
	 * Returns true if the given data is valid under the validation object
	 * @param data - The data to validate
	 * @return - True if valid, false otherwise
	 */
	public boolean valid(Object data);
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public JSONObject toJSONOBJ();
}
