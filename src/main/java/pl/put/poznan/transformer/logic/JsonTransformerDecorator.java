package pl.put.poznan.transformer.logic;


import java.io.IOException;


/**
 * Abstract base decorator class for JSON transformers.
 * Implements the decorator pattern to allow dynamic extension of JSON transformation functionality.
 * All concrete decorators should extend this class.
 */
public abstract class JsonTransformerDecorator implements JsonTransformer {
    protected final JsonTransformer component;

    /**
     * Constructs a new decorator with the specified JSON transformer component.
     *
     * @param component The JSON transformer component to be decorated
     */
    protected JsonTransformerDecorator(JsonTransformer component) {
        this.component = component;
    }

    /**
     * Transforms the input JSON string by delegating to the wrapped component.
     *
     * @param json The input JSON string to be transformed
     * @return The transformed JSON string
     * @throws IOException If an I/O error occurs during the transformation process
     */
    @Override
    public String transform(String json) throws IOException {
        // delegate first
        return component.transform(json);
    }
}