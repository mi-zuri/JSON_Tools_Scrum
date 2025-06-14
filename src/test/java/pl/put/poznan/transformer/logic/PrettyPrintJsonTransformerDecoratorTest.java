package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PrettyPrintJsonTransformerDecoratorTest {
    private JsonTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new PrettyPrintJsonTransformerDecorator(new RawJsonTransformer());
    }

    @Test
    void validTransformation() throws Exception {
        String expected = "{\n" +
                "  \"glossary\" : {\n" +
                "    \"title\" : \"example glossary\",\n" +
                "    \"GlossDiv\" : {\n" +
                "      \"title\" : \"S\",\n" +
                "      \"GlossList\" : {\n" +
                "        \"GlossEntry\" : {\n" +
                "          \"ID\" : \"SGML\",\n" +
                "          \"SortAs\" : \"SGML\",\n" +
                "          \"GlossTerm\" : \"Standard Generalized Markup Language\",\n" +
                "          \"Acronym\" : \"SGML\",\n" +
                "          \"Abbrev\" : \"ISO 8879:1986\",\n" +
                "          \"GlossDef\" : {\n" +
                "            \"para\" : \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                "            \"GlossSeeAlso\" : [ \"GML\", \"XML\" ]\n" +
                "          },\n" +
                "          \"GlossSee\" : \"markup\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String exampleJson = "{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}";
        assertEquals(expected, transformer.transform(exampleJson));
    }

    @Test
    void invalidTransformation() {
        String exampleJson = "invalid json";
        assertThrows(Exception.class, () -> transformer.transform(exampleJson));
    }

    @Test
    void nullTransformation() throws IOException{
        String exampleJson = null;
        assertNull(transformer.transform(exampleJson));
    }

}