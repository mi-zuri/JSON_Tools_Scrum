package pl.put.poznan.transformer.logic;

import org.json.JSONObject;

public class JsonMinimizer implements JsonTransformer{
    private final JSONObject jsonObject;

    public JsonMinimizer(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }
    @Override
    public JSONObject transform(JSONObject jsonObject){
        return this.jsonObject;
    }
}
