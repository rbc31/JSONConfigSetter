package config.validation;

import org.json.JSONObject;

public class GreaterThan0 implements ValidationObject {

	@Override
	public boolean valid(Object data) {
		if ((data instanceof Double && ((Double) data) > 0) ||
			(data instanceof Integer && ((Integer) data) > 0))	{
			return true;
		}
		return false;
	}

	@Override
	public JSONObject toJSONOBJ() {
		JSONObject toReturn = new JSONObject();
		
		toReturn.put("name", "GreaterThan0");
		return toReturn;
	}

}
