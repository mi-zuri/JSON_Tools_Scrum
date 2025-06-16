package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilterKeysJsonTransformerDecoratorTest {

    @Test
    void validTransformation() throws Exception {
        String exampleJson = "{\n" +
                "    \"glossary\": {\n" +
                "        \"title\": \"example glossary\",\n" +
                "        \"GlossDiv\": {\n" +
                "            \"title\": \"S\",\n" +
                "            \"GlossList\": {\n" +
                "                \"GlossEntry\": {\n" +
                "                    \"ID\": \"SGML\",\n" +
                "                    \"SortAs\": \"SGML\",\n" +
                "                    \"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
                "                    \"Acronym\": \"SGML\",\n" +
                "                    \"Abbrev\": \"ISO 8879:1986\",\n" +
                "                    \"GlossDef\": {\n" +
                "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                "                        \"GlossSeeAlso\": [\"GML\", \"XML\"]\n" +
                "                    },\n" +
                "                    \"GlossSee\": \"markup\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String expected = "{\"glossary\":{\"title\":\"example glossary\"}}";

        JsonTransformer transformer = new FilterKeysJsonTransformerDecorator(new RawJsonTransformer(), Set.of("glossary", "title"));
        String returned = transformer.transform(exampleJson);
        assertEquals(expected, returned);
    }

    @Test
    void invalidTransformation() {
        String exampleJson = "invalid json";
        JsonTransformer transformer = new FilterKeysJsonTransformerDecorator(new RawJsonTransformer(), Set.of("glossary", "title"));
        assertThrows(Exception.class, () -> transformer.transform(exampleJson));
    }


//    @Test
//    void emptyTransformation() throws Exception {
//        String exampleJson = "{}";
//        JsonTransformer transformer = new FilterKeysJsonTransformerDecorator(new RawJsonTransformer(), Set.of("glossary", "title"));
//        String returned = transformer.transform(exampleJson);
//        assertThrows(IllegalArgumentException.class, () -> transformer.transform(exampleJson));
//    }

    @Test
    void emptySetTransformation() {
        String exampleJson = "{\n" +
                "    \"glossary\": {\n" +
                "        \"title\": \"example glossary\"\n" +
                "    }\n" +
                "}";

        assertThrows(IllegalArgumentException.class, () -> new FilterKeysJsonTransformerDecorator(new RawJsonTransformer(), Set.of()));
    }

}