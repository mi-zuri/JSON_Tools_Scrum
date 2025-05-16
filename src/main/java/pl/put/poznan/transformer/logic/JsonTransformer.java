package pl.put.poznan.transformer.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class JsonTransformer {
    // i want to keep this original json - do not change it in transform!
    protected String json;
    public JsonTransformer(String json){
        this.json = json;
    }

    public void setJson(String json){
        this.json = json;
    }

    public String getJson(){
        return this.json;
    }

    public String decode() {
        return URLDecoder.decode(json, StandardCharsets.UTF_8);
    }

    public String encode() {
        return URLEncoder.encode(json, StandardCharsets.UTF_8);
    }
    public String transform(){
        return this.json;
    };
}
