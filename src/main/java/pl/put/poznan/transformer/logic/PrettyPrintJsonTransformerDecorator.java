package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;


/**
 * Decorator for JSON transformers that improves the readability of JSON output.
 * This decorator applies pretty-printing formatting to the JSON string, making it
 * more human-readable with proper indentation and line breaks.
 */
public class PrettyPrintJsonTransformerDecorator extends JsonTransformerDecorator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new pretty-print decorator with the specified JSON transformer component.
     *
     * @param component The JSON transformer component to be decorated with pretty-printing functionality
     */
    public PrettyPrintJsonTransformerDecorator(JsonTransformer component) {
        super(component);
    }

    /**
     * Transforms the input JSON string by first applying the wrapped component's transformation,
     * then formatting the result with pretty-printing for improved readability.
     *
     * @param json The input JSON string to be transformed and pretty-printed
     * @return The transformed and pretty-printed JSON string with proper indentation
     * @throws IOException If an I/O error occurs during JSON parsing, transformation, or formatting
     */
    @Override
    public String transform(String json) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            String fullJson = super.transform(json);
            JsonNode node = objectMapper.readTree(fullJson);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        }
        catch (IOException e) {
            throw new IOException("Error formatting JSON", e);
        }
    }
}