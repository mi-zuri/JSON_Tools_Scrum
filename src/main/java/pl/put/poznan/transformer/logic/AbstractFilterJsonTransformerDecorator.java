package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;

/**
 * Abstract base class for JSON filter decorators that provides common filtering functionality.
 * This class implements the Template Method pattern, allowing subclasses to define
 * their specific key filtering strategies while reusing the common node processing logic.
 */
public abstract class AbstractFilterJsonTransformerDecorator extends JsonTransformerDecorator {
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Set<String> keys;

    /**
     * Constructs a new abstract filter decorator with the specified JSON transformer component and keys.
     *
     * @param component The JSON transformer component to be decorated with filtering functionality
     * @param keys Set of key names used for filtering (meaning depends on concrete implementation)
     * @throws IllegalArgumentException if keys is null or empty
     */
    protected AbstractFilterJsonTransformerDecorator(JsonTransformer component, Set<String> keys) {
        super(component);
        if (keys == null || keys.isEmpty()) {
            throw new IllegalArgumentException("Keys set cannot be null or empty");
        }
        this.keys = keys;
    }

    /**
     * Transforms the input JSON string by first applying the wrapped component's transformation,
     * then filtering the JSON according to the specific implementation strategy.
     * Preserves the original formatting structure of the JSON.
     *
     * @param json The input JSON string to be transformed and filtered
     * @return The transformed and filtered JSON string with preserved formatting
     * @throws IOException If an I/O error occurs during JSON parsing, transformation, or filtering
     */
    @Override
    public String transform(String json) throws IOException {
        try {
            String fullJson = super.transform(json);
            JsonNode node = objectMapper.readTree(fullJson);
            JsonNode filteredNode = filterNode(node);

            return objectMapper.writeValueAsString(filteredNode);
        } catch (IOException e) {
            throw new IOException(getErrorMessage() + e.getMessage(), e);
        }
    }

    /**
     * Recursively filters a JSON node according to the specific filtering strategy.
     *
     * @param node The JSON node to filter
     * @return The filtered JSON node
     */
    private JsonNode filterNode(JsonNode node) {
        if (node.isObject()) {
            var filteredObject = objectMapper.createObjectNode();
            node.fields().forEachRemaining(entry -> {
                if (shouldIncludeKey(entry.getKey())) {
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

    /**
     * Template method that determines whether a specific key should be included in the filtered result.
     * Concrete implementations must provide their specific filtering logic.
     *
     * @param key The key name to evaluate
     * @return true if the key should be included in the filtered result, false otherwise
     */
    protected abstract boolean shouldIncludeKey(String key);

    /**
     * Template method that provides the error message prefix for IOException handling.
     * Concrete implementations should provide their specific error context.
     *
     * @return The error message prefix for this filter type
     */
    protected abstract String getErrorMessage();
}