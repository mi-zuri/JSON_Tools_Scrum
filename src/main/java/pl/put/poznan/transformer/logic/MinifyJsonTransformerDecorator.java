package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Decorator for JSON transformers that minifies the JSON output by removing unnecessary whitespace.
 * This decorator compacts the JSON string to its minimal form, removing all non-essential
 * whitespace characters including spaces, tabs, and newlines between JSON elements.
 */
public class MinifyJsonTransformerDecorator extends JsonTransformerDecorator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new minify decorator with the specified JSON transformer component.
     *
     * @param component The JSON transformer component to be decorated with minification functionality
     */
    public MinifyJsonTransformerDecorator(JsonTransformer component) {
        super(component);
    }

    /**
     * Transforms the input JSON string by first applying the wrapped component's transformation,
     * then minifying the result by removing all unnecessary whitespace characters.
     *
     * @param json The input JSON string to be transformed and minified
     * @return The transformed and minified JSON string without unnecessary whitespace
     * @throws IOException If an I/O error occurs during JSON parsing, transformation, or minification
     */
    @Override
    public String transform(String json) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            String fullJson = super.transform(json);
            JsonNode node = objectMapper.readTree(fullJson);
            return objectMapper.writeValueAsString(node);
        }
        catch (IOException e) {
            throw new IOException("Error formatting JSON", e);
        }
    }
}