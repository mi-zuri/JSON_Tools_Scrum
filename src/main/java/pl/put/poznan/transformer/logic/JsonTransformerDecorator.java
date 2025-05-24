package pl.put.poznan.transformer.logic;


import java.io.IOException;

/**
 * Abstract decorator class for JSON transformers.
 * This class implements the JsonTransformer interface and delegates the transformation
 * to a component of type JsonTransformer.
 */
// Base Decorator
public abstract class JsonTransformerDecorator implements JsonTransformer {
    protected final JsonTransformer component;

    protected JsonTransformerDecorator(JsonTransformer component) {
        this.component = component;
    }

    @Override
    public String transform(String json) throws IOException {
        // delegate first
        return component.transform(json);
    }
}