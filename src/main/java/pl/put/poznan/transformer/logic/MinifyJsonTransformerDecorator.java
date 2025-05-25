package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Decorator for JSON transformers that minifies the JSON output.
 * This class extends JsonTransformerDecorator and overrides the transform method
 * to remove unnecessary whitespace from the JSON string.
 */
public class MinifyJsonTransformerDecorator extends JsonTransformerDecorator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MinifyJsonTransformerDecorator(JsonTransformer component) {
        super(component);
    }

    @Override
    public String transform(String json) throws IOException {
        // first get the “raw” or previously-decorated JSON
        String fullJson = super.transform(json);
        // parse & write without any extra whitespace
        JsonNode node = objectMapper.readTree(fullJson);
        return objectMapper.writeValueAsString(node);
    }
}