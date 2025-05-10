package pl.put.poznan.transformer.rest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
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
    private JSONObject decodedJson;

    @PostMapping("/set")
    public RedirectView makeUrlCompatible(@RequestParam String jsonInput) {
        String decodedJson = decode(jsonInput);

        String encodedJson = encode(decodedJson);

        // this is to show the json is holding newline characters
        System.out.println(decodedJson);

        this.decodedJson = new JSONObject(decodedJson);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/choose-option");
        return redirectView;
    }

    private String decode(String jsonInput) {
        return URLDecoder.decode(jsonInput, StandardCharsets.UTF_8);
    }

    private String encode(String jsonInput) {
        return URLEncoder.encode(jsonInput, StandardCharsets.UTF_8);
    }

    @PostMapping("/process-options")
    public RedirectView ProcessOptions(
            @RequestParam(value = "minimize", required = false) String minimize,
            @RequestParam(value = "unminimize", required = false) String unminimize,
            @RequestParam(value = "includeChars", required = false) String includeChars,
            @RequestParam(value = "excludeChars", required = false) String excludeChars){

        List<JsonTransformer> transformers = new ArrayList<JsonTransformer>();
        if (minimize!=null)
            transformers.add(new JsonMinimizer(this.decodedJson));
        // todo for rest of the objects

        for (JsonTransformer transformer: transformers){
            this.decodedJson = transformer.transform();
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/final-json?encodedJson=" + encode(this.decodedJson.toString()));
        return redirectView;
    }

    @PostMapping("/reset")
    public RedirectView Reset() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("");
        return redirectView;
    }




}


