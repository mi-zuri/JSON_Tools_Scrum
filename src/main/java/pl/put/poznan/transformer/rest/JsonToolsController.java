package pl.put.poznan.transformer.rest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.model.IProcessableElementTag;
import pl.put.poznan.transformer.logic.Json;
import pl.put.poznan.transformer.logic.JsonMinimizer;
import pl.put.poznan.transformer.logic.JsonTransformer;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * REST API controller for JSON transformation tools.
 * Provides endpoints for:
 * - Receiving and processing JSON input
 * - Applying various JSON transformations (minimize, unminimize, etc.)
 * - Retrieving original JSON
 * - Getting available transformation options
 */
@RestController
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);
    private Json json;

    
    /**
     * Endpoint for receiving and processing JSON input.
     * Stores the provided JSON for further transformations and returns encoded version.
     *
     * @param jsonInput The JSON string to be processed, received in request body
     * @return ResponseEntity containing URL-encoded JSON string
     */
    @RequestMapping(path = "/input", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> processJsonInput(@RequestBody String jsonInput) {
        logger.info("Received JSON input request");

        json = new Json(jsonInput);
        String decodedJson = json.decode();
        logger.debug("Decoded JSON: {}", decodedJson);
        return ResponseEntity.ok(json.encode());

    }
    
    /**
     * Endpoint for applying various JSON transformations.
     *
     * @param minimize Flag indicating whether the JSON should be minimized
     * @param unminimize Flag indicating whether the JSON should be unminimized
     * @param includeChars List of characters to include in the JSON
     * @param excludeChars List of characters to exclude from the JSON
     * @return URL-encoded JSON string after applying the specified transformations
    */
    @RequestMapping(path = "/transform", method = RequestMethod.GET, produces = "application/json")
    public String transform(@RequestParam(value = "minimize", required = false) String minimize,
                            @RequestParam(value = "unminimize", required = false) String unminimize,
                            @RequestParam(value = "includeChars", required = false) String includeChars,
                            @RequestParam(value = "excludeChars", required = false) String excludeChars){
        List<JsonTransformer> transformers = new ArrayList<JsonTransformer>();
        String newJson = json.decode();
        if (minimize!=null)
            transformers.add(new JsonMinimizer(newJson));
        // todo for rest of the objects
        for (JsonTransformer transformer: transformers){
            newJson = transformer.transform();
        }
        return newJson;
    }


    /**
     * Endpoint for retrieving the original JSON data.
     * Returns the JSON content in its unmodified form as it was last stored through the /input endpoint.
     * Does not apply any transformations or formatting to the returned JSON string.
     *
     * @return The original JSON string without any modifications
     * @throws IllegalStateException if no JSON has been stored yet
     * @see #processJsonInput(String)
     * @see Json#getJson()
     */
    @RequestMapping(path = "/original-json", method = RequestMethod.GET, produces = "application/json")
    public String returnOriginalJson(){
        return json.getJson();
    }

    /**
     * Endpoint for retrieving available JSON transformation options.
     * Returns a list of supported transformation operations that can be applied to JSON data.
     * Currently supports:
     * - minimize: Removes unnecessary whitespace and formatting
     *
     * @return List of strings containing available transformation options
     */
    @RequestMapping(path = "/options", method = RequestMethod.GET, produces = "application/json")
    public List<String> returnOptions(){
        List<String> options = new ArrayList<String>();
        options.add("minimize");
       return options;
    }

}


