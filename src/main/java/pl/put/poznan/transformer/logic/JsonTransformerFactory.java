package pl.put.poznan.transformer.logic;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Factory class responsible for creating appropriate JSON transformer instances.
 * This class implements the Factory pattern to provide different types of JSON transformers
 * based on the requested transformation type and optional parameters
 */
@Component
public class JsonTransformerFactory {
    /**
     * Creates and returns a JSON transformer based on the specified transformation type.
     *
     * @param type The type of transformation requested (e.g., "minify", "pretty")
     * @return A JsonTransformer instance configured for the requested transformation type
     */
    public JsonTransformer getTransformer(String type) {
        JsonTransformer base = new RawJsonTransformer();

        switch (type.toLowerCase()) {
            case "minify":
                return new MinifyJsonTransformerDecorator(base);
            case "pretty":
                return new PrettyPrintJsonTransformerDecorator(base);
            default:
                return base;
        }
    }

    /**
     * Creates and returns a JSON transformer based on the specified transformation type with additional parameters.
     * This overloaded method supports transformations that require additional configuration, such as key filtering.
     *
     * @param type The type of transformation requested (e.g., "minify", "pretty", "filter")
     * @param allowedKeys Set of keys to retain when using "filter" transformation type (ignored for other types)
     * @return A JsonTransformer instance configured for the requested transformation type
     * @throws IllegalArgumentException if filter type is requested but allowedKeys is null or empty
     */
    public JsonTransformer getTransformer(String type, Set<String> allowedKeys) {
        JsonTransformer base = new RawJsonTransformer();

        switch (type.toLowerCase()) {
            case "minify":
                return new MinifyJsonTransformerDecorator(base);
            case "pretty":
                return new PrettyPrintJsonTransformerDecorator(base);
            case "filter":
                if (allowedKeys == null || allowedKeys.isEmpty()) {
                    throw new IllegalArgumentException("Filter transformation requires non-empty set of allowed keys");
                }
                return new FilterKeysJsonTransformerDecorator(base, allowedKeys);
            default:
                return base;
        }
    }
}
