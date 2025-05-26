package pl.put.poznan.transformer.logic;

import java.io.IOException;

/**
 * Component interface for transforming JSON data.
 * Implementations of this interface provide different JSON transformation strategies.
 */
public interface JsonTransformer {
    /**
     * Transforms the input JSON string according to the specific implementation strategy.
     *
     * @param json The input JSON string to be transformed
     * @return The transformed JSON string
     * @throws IOException If an I/O error occurs during the transformation process
     */
    String transform(String json) throws IOException;
}