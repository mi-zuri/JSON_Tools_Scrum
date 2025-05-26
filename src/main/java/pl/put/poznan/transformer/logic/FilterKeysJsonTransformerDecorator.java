package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;

/**
 * Decorator for JSON transformers that filters JSON objects to retain only specified keys.
 * This decorator removes all keys from JSON objects except those specified in the allowed keys set,
 * while preserving the overall structure and shape of the JSON data.
 *
 * <p>Uses Jackson's built-in filtering capabilities for efficient key filtering.</p>
 */
public class FilterKeysJsonTransformerDecorator extends JsonTransformerDecorator {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<String> allowedKeys;

    /**
     * Constructs a new filter keys decorator with the specified JSON transformer component and allowed keys.
     *
     * @param component The JSON transformer component to be decorated with key filtering functionality
     * @param allowedKeys Set of key names that should be retained in JSON objects
     * @throws IllegalArgumentException if allowedKeys is null or empty
     */
    public FilterKeysJsonTransformerDecorator(JsonTransformer component, Set<String> allowedKeys) {
        super(component);
        if (allowedKeys == null || allowedKeys.isEmpty()) {
            throw new IllegalArgumentException("Allowed keys set cannot be null or empty");
        }
        this.allowedKeys = allowedKeys;
    }

    /**
     * Transforms the input JSON string by first applying the wrapped component's transformation,
     * then filtering all JSON objects to retain only the specified keys while preserving the data shape.
     *
     * @param json The input JSON string to be transformed and filtered
     * @return The transformed JSON string with only allowed keys in all objects
     * @throws IOException If an I/O error occurs during JSON parsing, transformation, or if the input JSON is invalid
     */
    @Override
    public String transform(String json) throws IOException {
        try {
            String fullJson = super.transform(json);
            JsonNode node = objectMapper.readTree(fullJson);
            return objectMapper.writeValueAsString(filterNode(node));
        } catch (IOException e) {
            throw new IOException("Error filtering JSON keys - invalid JSON format: " + e.getMessage(), e);
        }
    }

    /**
     * Recursively filters a JSON node to keep only allowed keys.
     *
     * @param node The JSON node to filter
     * @return The filtered JSON node
     */
    private JsonNode filterNode(JsonNode node) {
        if (node.isObject()) {
            var filteredObject = objectMapper.createObjectNode();
            node.fields().forEachRemaining(entry -> {
                if (allowedKeys.contains(entry.getKey())) {
                    // Recursively filter the value
                    filteredObject.set(entry.getKey(), filterNode(entry.getValue()));
                }
            });
            return filteredObject;
        } else if (node.isArray()) {
            var filteredArray = objectMapper.createArrayNode();
            node.forEach(element -> filteredArray.add(filterNode(element)));
            return filteredArray;
        } else {
            // Primitive values (string, number, boolean, null) - return as-is
            return node;
        }
    }
}