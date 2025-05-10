package pl.put.poznan.transformer.logic;

import org.json.JSONObject;

public class JsonMinimizer extends JsonTransformer{
    private final JSONObject jsonObject;

    public JsonMinimizer(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }
    @Override
    public JSONObject transform(){
        return this.jsonObject;
    }
}
