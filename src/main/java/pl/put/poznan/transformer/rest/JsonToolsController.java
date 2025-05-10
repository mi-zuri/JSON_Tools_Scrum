package pl.put.poznan.transformer.rest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.put.poznan.transformer.logic.JsonMinimizer;
import pl.put.poznan.transformer.logic.JsonTransformer;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);

    @RequestMapping(value="/trans", method = RequestMethod.GET, produces = "application/json")
    public JSONObject get(@RequestParam("json") JSONObject text) {

        // log the parameters
        logger.debug(text.toString());
        //logger.debug(Arrays.toString(transforms));

        // perform the transformation, you should run your logic here, below is just a silly example
        JsonTransformer transformer = new JsonMinimizer(text);
        return transformer.transform(text);
    }


    @GetMapping("/options")
    public List<String> allTransformerNames() {
        return List.of("minimizer");
    }


    @GetMapping("/transform")                    // ①
    public String transform(
            @RequestParam("json") String json,   // ②
            @RequestParam("ops")  List<String> ops) {
        return "json = " + json + ", ops = " + ops;
    }

    @PostMapping("/set")
    public RedirectView makeUrlCompatible(@RequestParam String jsonInput) {
        String decodedJson = decode(jsonInput);

        String encodedJson = encode(decodedJson);

        // this is to show the json is holding newline characters
        System.out.println(decodedJson);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/choose-option?encodedJson=" + encodedJson);
        return redirectView;
    }

    private String decode(String jsonInput) {
        return URLDecoder.decode(jsonInput, StandardCharsets.UTF_8);
    }

    private String encode(String jsonInput) {
        return URLEncoder.encode(jsonInput, StandardCharsets.UTF_8);
    }

    @GetMapping("/choose-option")
    public String transform(
            @RequestParam("encodedJson") String json) {
        //TODO chosing option
        return "json = " + json;
    }



}


