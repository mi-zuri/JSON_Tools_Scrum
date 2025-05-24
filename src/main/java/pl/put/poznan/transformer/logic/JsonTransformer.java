package pl.put.poznan.transformer.logic;

import java.io.IOException;

// Component interface for transforming JSON data.

public interface JsonTransformer {
    String transform(String json) throws IOException;
}