package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonTransformerFactoryTest {
    private JsonTransformerFactory factory;

    @BeforeEach
    void setUp() {
        factory = new JsonTransformerFactory();
    }

    @Test
    void createRawJsonTransformer() {
        JsonTransformer transformer = factory.getTransformer("raw");
        assertNotNull(transformer);
        assertTrue(transformer instanceof RawJsonTransformer);
    }

    @Test
    void createPrettyPrintJsonTransformer() {
        JsonTransformer transformer = factory.getTransformer("pretty");
        assertNotNull(transformer);
        assertTrue(transformer instanceof PrettyPrintJsonTransformerDecorator);
    }

    @Test
    void createMinifyJsonTransformer() {
        JsonTransformer transformer = factory.getTransformer("minify");
        assertNotNull(transformer);
        assertTrue(transformer instanceof MinifyJsonTransformerDecorator);
    }

    @Test
    void createFilterKeysJsonTransformer() {
        JsonTransformer transformer = factory.getTransformer("filter", Set.of("key1", "key2"));
        assertNotNull(transformer);
        assertTrue(transformer instanceof FilterKeysJsonTransformerDecorator);
    }

    @Test
    void createFilterKeysJsonTransformerWithEmptySet() {
        assertThrows(IllegalArgumentException.class, () -> factory.getTransformer("filter", Set.of()));
    }

}