package pl.put.poznan.transformer.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Base class for JSON transformation logic.
 * Provides foundation for different JSON transformation strategies.
 * Maintains the original JSON input and implements basic transformation functionality
 * that can be overridden by specific transformers.
 */
public class JsonTransformer {
    protected Json json;
    
    /**
     * Creates a JsonTransformer instance with a provided Json object.
     *
     * @param json The Json object to be transformed
     */
    public JsonTransformer(Json json) {
        this.json = json;
    }

    /**
     * Creates a JsonTransformer instance with a provided JSON string.
     * Automatically converts the string into a Json object.
     *
     * @param json The JSON string to be transformed
     */
    public JsonTransformer(String json){
        this.json = new Json(json);
    }

    /* Override this method in subclasses to implement specific transformation logic */

    public String transform(){
        return this.json.getJson();
    };
}
