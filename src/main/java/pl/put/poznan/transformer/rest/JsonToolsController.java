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
     * @param type      The type of transformation (e.g., "minify", "pretty", "raw", "filter", "filter-out") applied in sequence separated by comma
     * @param includeKeys Comma-separated list of keys to retain for "filter" operations (optional)
     * @param excludeKeys Comma-separated list of keys to remove for "filter-out" operations (optional)
     * @return ResponseEntity containing the transformed JSON string
     */
    @PostMapping(path = "/transform", produces = "application/json")
    public ResponseEntity<String> transformJson(@RequestBody(required = false) String jsonInput,
                                                @RequestParam(value = "type", required = false, defaultValue = "raw") List<String> type,
                                                @RequestParam(value = "includeKeys", required = false) String includeKeys,
                                                @RequestParam(value = "excludeKeys", required = false) String excludeKeys) {
        if (jsonInput == null) {
            logger.error("Empty jsonInput");
            return ResponseEntity.badRequest().body("Error during JSON transformation, please provide a JSON input in the body");
        }

        logger.info("Received JSON transformation request with type: {}", type);
        if (includeKeys != null) {
            logger.info("Include keys parameter specified: {}", includeKeys);
        }
        if (excludeKeys != null) {
            logger.info("Exclude keys parameter specified: {}", excludeKeys);
        }

        // Parse include keys for filter operations
        Set<String> includeKeySet = null;
        if (includeKeys != null && !includeKeys.trim().isEmpty()) {
            includeKeySet = new HashSet<>(Arrays.asList(includeKeys.split("\\s*,\\s*")));
            logger.debug("Parsed include keys: {}", includeKeySet);
        }

        // Parse exclude keys for filter-out operations
        Set<String> excludeKeySet = null;
        if (excludeKeys != null && !excludeKeys.trim().isEmpty()) {
            excludeKeySet = new HashSet<>(Arrays.asList(excludeKeys.split("\\s*,\\s*")));
            logger.debug("Parsed exclude keys: {}", excludeKeySet);
        }

        // Get the appropriate transformer from the factory
        String transformedJson = jsonInput;
        for (String transformation : type) {
            JsonTransformer transformer;

            try {
                // Check if this transformation requires keys parameter
                if ("filter".equalsIgnoreCase(transformation)) {
                    if (includeKeySet == null || includeKeySet.isEmpty()) {
                        logger.error("Filter transformation requested but no includeKeys provided");
                        return ResponseEntity.badRequest().body("Filter transformation requires 'includeKeys' parameter with comma-separated key names");
                    }
                    transformer = transformerFactory.getTransformer(transformation, includeKeySet);
                } else if ("filter-out".equalsIgnoreCase(transformation)) {
                    if (excludeKeySet == null || excludeKeySet.isEmpty()) {
                        logger.error("Filter-out transformation requested but no excludeKeys provided");
                        return ResponseEntity.badRequest().body("Filter-out transformation requires 'excludeKeys' parameter with comma-separated key names");
                    }
                    transformer = transformerFactory.getTransformer(transformation, excludeKeySet);
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
        return Arrays.asList("minify", "pretty", "filter", "filter-out", "raw");
    }
}