package pl.put.poznan.transformer.logic;

import java.util.Set;

/**
 * Decorator for JSON transformers that removes specified keys from JSON objects.
 * This decorator filters out all specified keys from JSON objects while keeping all others,
 * and preserves the overall structure of the JSON data.
 *
 * <p>Formatting is altered: JSON is minified</p>
 */
public class FilterOutKeysJsonTransformerDecorator extends AbstractFilterJsonTransformerDecorator {

    /**
     * Constructs a new filter-out decorator with the specified JSON transformer component and keys to remove.
     *
     * @param component The JSON transformer component to be decorated with key removal functionality
     * @param keysToRemove Set of key names that should be removed from JSON objects
     * @throws IllegalArgumentException if keysToRemove is null or empty
     */
    public FilterOutKeysJsonTransformerDecorator(JsonTransformer component, Set<String> keysToRemove) {
        super(component, keysToRemove);
    }

    /**
     * Determines whether a key should be included based on the keys to remove set.
     *
     * @param key The key name to evaluate
     * @return true if the key is NOT in the keys to remove set, false otherwise
     */
    @Override
    protected boolean shouldIncludeKey(String key) {
        return !keys.contains(key);
    }

    /**
     * Provides the error message prefix for filter-out operations.
     *
     * @return The error message prefix
     */
    @Override
    protected String getErrorMessage() {
        return "Error filtering out JSON keys - invalid JSON format: ";
    }
}