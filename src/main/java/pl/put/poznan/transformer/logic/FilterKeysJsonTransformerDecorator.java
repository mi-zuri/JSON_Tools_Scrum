package pl.put.poznan.transformer.logic;

import java.util.Set;

/**
 * Decorator for JSON transformers that filters JSON objects to retain only specified keys.
 * This decorator removes all keys from JSON objects except those specified in the allowed keys set,
 * while preserving the overall structure of the JSON data.
 *
 * <p>Formatting is altered: JSON is minified</p>
 */
public class FilterKeysJsonTransformerDecorator extends AbstractFilterJsonTransformerDecorator {

    /**
     * Constructs a new filter keys decorator with the specified JSON transformer component and allowed keys.
     *
     * @param component The JSON transformer component to be decorated with key filtering functionality
     * @param allowedKeys Set of key names that should be retained in JSON objects
     * @throws IllegalArgumentException if allowedKeys is null or empty
     */
    public FilterKeysJsonTransformerDecorator(JsonTransformer component, Set<String> allowedKeys) {
        super(component, allowedKeys);
    }

    /**
     * Determines whether a key should be included based on the allowed keys set.
     *
     * @param key The key name to evaluate
     * @return true if the key is in the allowed keys set, false otherwise
     */
    @Override
    protected boolean shouldIncludeKey(String key) {
        return keys.contains(key);
    }

    /**
     * Provides the error message prefix for filter operations.
     *
     * @return The error message prefix
     */
    @Override
    protected String getErrorMessage() {
        return "Error filtering JSON keys - invalid JSON format: ";
    }
}