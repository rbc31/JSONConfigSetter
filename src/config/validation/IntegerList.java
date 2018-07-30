package config.validation;

import java.util.ArrayList;

import org.json.JSONObject;

import config.Data;
import config.ETYPE;

public class IntegerList implements ValidationObject {

	@Override
	public boolean valid(Object data) {
		if (data instanceof ArrayList<?>) {
			ArrayList<?> list = (ArrayList<?>) data;
			for (Object subData : list) {
				if (subData instanceof Data) {
					if (((Data) subData).getType() == ETYPE.INTEGER) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public JSONObject toJSONOBJ() {
		JSONObject toReturn = new JSONObject();
		
		toReturn.put("name", "IntegerList");
		return toReturn;
	}

}
