package pl.put.poznan.transformer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.transformer.logic.JsonTransformer;
import pl.put.poznan.transformer.logic.JsonTransformerFactory;

import java.io.IOException;
import java.util.*;

/**
 * REST API controller for JSON transformation tools.
 */
@RestController
@RequestMapping("/json-tools")
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);
    private final JsonTransformerFactory transformerFactory;

    public JsonToolsController(JsonTransformerFactory transformerFactory) {
        this.transformerFactory = transformerFactory;
    }

    /**
     * Endpoint for processing JSON input and applying transformations. If an error occurs during the transformation process, it prints the message to the log.
     *
     * @param jsonInput The JSON string to be processed, received in request body
     * @param type      The type of transformation (e.g., "minify", "pretty", "raw") applied in sequence separated by comma
     * @param keys      Comma-separated list of keys to retain when using "filter" transformation (optional, only used with filter type)
     * @return ResponseEntity containing the transformed JSON string
     */
    @PostMapping(path = "/transform", produces = "application/json")
    public ResponseEntity<String> transformJson(@RequestBody(required = false) String jsonInput,
                                                @RequestParam(value = "type", required = false, defaultValue = "raw") List<String> type,
                                                @RequestParam(value = "keys", required = false) String keys) {
        if (jsonInput == null) {
            logger.error("Empty jsonInput");
            return ResponseEntity.badRequest().body("Error during JSON transformation, please provide a JSON input in the body");

        }
        logger.info("Received JSON transformation request with type: {}", type);

        if (keys != null) {
            logger.info("Filter keys specified: {}", keys);
        }

        // Parse allowed keys for filter transformation
        Set<String> allowedKeys = null;
        if (keys != null && !keys.trim().isEmpty()) {
            allowedKeys = new HashSet<>(Arrays.asList(keys.split("\\s*,\\s*")));
            logger.debug("Parsed allowed keys: {}", allowedKeys);
        }

        // Get the appropriate transformer from the factory
        String transformedJson = jsonInput;
        for (String transformation: type) {
            JsonTransformer transformer;

            try {
                // Check if this transformation is "filter" and requires keys
                if ("filter".equalsIgnoreCase(transformation)) {
                    if (allowedKeys == null || allowedKeys.isEmpty()) {
                        logger.error("Filter transformation requested but no keys provided");
                        return ResponseEntity.badRequest().body("Filter transformation requires 'keys' parameter with comma-separated key names");
                    }
                    transformer = transformerFactory.getTransformer(transformation, allowedKeys);
                } else {
                    transformer = transformerFactory.getTransformer(transformation);
                }

                transformedJson = transformer.transform(transformedJson);

            } catch (IllegalArgumentException e) {
                logger.error("Invalid transformation parameters: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Invalid transformation parameters: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Error during JSON transformation: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Error during JSON transformation, verify your JSON input: " + e.getMessage());
            }
        }

        logger.debug("Transformed JSON: {}", transformedJson);
        return ResponseEntity.ok(transformedJson);
    }

    /**
     * Endpoint for retrieving available JSON transformation options.
     *
     * @return List of supported transformation types
     */
    @GetMapping(path = "/options", produces = "application/json")
    public List<String> getTransformationOptions() {
        List<String> options = new ArrayList<>();
        options.add("minify");
        options.add("pretty");
        options.add("filter");
        options.add("raw");
        return options;
    }

}