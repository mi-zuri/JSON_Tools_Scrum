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
import pl.put.poznan.transformer.logic.JsonMinimizer;
import pl.put.poznan.transformer.logic.JsonTransformer;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@RestController
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);
    private JsonTransformer json;

    //takes arguments as params, returns response entity with transposed json
    // it uses request body so you need to send it in the postman body
    @RequestMapping(path = "/input", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> processJsonInput(@RequestBody String jsonInput) {
        logger.info("Received JSON input request");

        json = new JsonTransformer(jsonInput);
        String decodedJson = json.decode();
        logger.debug("Decoded JSON: {}", decodedJson);
        return ResponseEntity.ok(json.encode());

    }
    @RequestMapping(path = "/transform", method = RequestMethod.GET, produces = "application/json")
    public String transform(@RequestParam(value = "minimize", required = false) String minimize,
                            @RequestParam(value = "unminimize", required = false) String unminimize,
                            @RequestParam(value = "includeChars", required = false) String includeChars,
                            @RequestParam(value = "excludeChars", required = false) String excludeChars){
        List<JsonTransformer> transformers = new ArrayList<JsonTransformer>();
        String newJson = json.getJson();
        if (minimize!=null)
            transformers.add(new JsonMinimizer(newJson));
        // todo for rest of the objects

        for (JsonTransformer transformer: transformers){
            newJson = transformer.transform();
        }
        return newJson;
    }


    @RequestMapping(path = "original-json", method = RequestMethod.GET, produces = "application/json")
    public String returnOriginalJson(){
        return json.getJson();
    }

}


