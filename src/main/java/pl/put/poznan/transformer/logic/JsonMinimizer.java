package pl.put.poznan.transformer.logic;

import org.json.JSONObject;

public class JsonMinimizer extends JsonTransformer{


    public JsonMinimizer(String jsonObject){
        super(jsonObject);
    }
    @Override
    public String transform(){
        return super.json;
    }
}
