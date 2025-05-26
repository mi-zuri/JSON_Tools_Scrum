package pl.put.poznan.transformer.logic;

import org.springframework.stereotype.Component;

/**
 * Factory class responsible for creating appropriate JSON transformer instances.
 * This class implements the Factory pattern to provide different types of JSON transformers
 * based on the requested transformation type.
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
}
