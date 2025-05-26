package pl.put.poznan.transformer.logic;

import java.io.IOException;

/**
 * Base concrete implementation of JsonTransformer that performs no transformation.
 * This class serves as the foundation component in the decorator pattern, providing
 * a pass-through implementation that returns the input JSON string unchanged.
 */
public class RawJsonTransformer implements JsonTransformer {
    /**
     * Transforms the input JSON string by returning it unchanged.
     * This implementation provides a no-operation transformation that serves
     * as the base component for the decorator pattern.
     *
     * @param json The input JSON string to be "transformed" (passed through unchanged)
     * @return The original input JSON string without any modifications
     */
    @Override
    public String transform(String json) {
        // no-op, just pass through
        return json;
    }
}