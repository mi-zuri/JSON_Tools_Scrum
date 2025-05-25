package pl.put.poznan.transformer.logic;

import pl.put.poznan.transformer.logic.JsonTransformer;

// Concrete component
public class RawJsonTransformer implements JsonTransformer {
    @Override
    public String transform(String json) {
        // no-op, just pass through
        return json;
    }
}