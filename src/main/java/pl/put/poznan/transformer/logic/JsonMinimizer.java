package pl.put.poznan.transformer.logic;

import org.json.JSONObject;

public class JsonMinimizer extends JsonTransformer{


    public JsonMinimizer(Json jsonObject){
        super(jsonObject);
    }
    public JsonMinimizer(String json){
        super(json);
    }
    @Override
    public String transform(){
        return super.json.getJson();
    }
}
