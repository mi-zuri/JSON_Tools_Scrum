package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;


/**
 * Decorator for JSON transformers that improves the readability of JSON output.
 *
 */
// Decorator for minifying JSON output
public class PrettyPrintJsonTransformerDecorator extends JsonTransformerDecorator {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PrettyPrintJsonTransformerDecorator(JsonTransformer component) {
        super(component);
    }

    @Override
    public String transform(String json) throws IOException {
        // Delegate to component (e.g., raw or previously decorated)
        String fullJson = super.transform(json);
        // Parse and pretty-print with default settings
        JsonNode node = objectMapper.readTree(fullJson);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
}