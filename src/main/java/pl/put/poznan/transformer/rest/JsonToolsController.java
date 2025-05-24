package pl.put.poznan.transformer.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.transformer.logic.JsonTransformer;
import pl.put.poznan.transformer.logic.JsonTransformerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * Endpoint for processing JSON input and applying transformations.
     *
     * @param jsonInput The JSON string to be processed, received in request body
     * @param type      The type of transformation (e.g., "minify", "pretty")
     * @return ResponseEntity containing the transformed JSON string
     */
    @PostMapping(path = "/transform", produces = "application/json")
    public ResponseEntity<String> transformJson(@RequestBody String jsonInput,
                                                @RequestParam(value = "type", required = false, defaultValue = "raw") String type) throws IOException {
        logger.info("Received JSON transformation request with type: {}", type);

        // Get the appropriate transformer from the factory
        JsonTransformer transformer = transformerFactory.getTransformer(type);

        // Apply the transformation
        String transformedJson = transformer.transform(jsonInput);

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
        options.add("raw");
        return options;
    }
}