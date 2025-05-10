package pl.put.poznan.transformer.logic;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public abstract class JsonTransformer {
    public abstract JSONObject transform();

}
