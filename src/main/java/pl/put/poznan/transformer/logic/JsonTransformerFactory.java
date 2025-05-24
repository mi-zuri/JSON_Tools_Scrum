package pl.put.poznan.transformer.logic;

import org.springframework.stereotype.Component;

@Component
public class JsonTransformerFactory {
    public JsonTransformer getTransformer(String type) {
        JsonTransformer base = new RawJsonTransformer();

        switch (type.toLowerCase()) {
            case "minify":
                return new MinifyJsonTransformerDecorator(base);
            case "unminify":
            case "pretty":
                return new PrettyPrintJsonTransformerDecorator(base);
            default:
                return base;
        }
    }
}
