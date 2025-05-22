package pl.put.poznan.transformer.logic;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Class representing a JSON object.
 * Provides basic functionality for encoding and decoding JSON strings.
 */
public class Json {
    private String json;


    /**
     * Creates a new Json object with the specified JSON string.
     *
     * @param json The JSON string to be stored
     */
    public Json(String json){
        this.json = json;
    }

    /**
     * Returns the stored JSON string.
     *
     * @return The current JSON string
     */
    public String getJson(){
        return this.json;
    }

    /**
     * Sets a new JSON string.
     *
     * @param json The new JSON string to store
     */
    public void setJson(String json){
        this.json = json;
    }

    /**
     * Decodes the stored JSON string from URL encoding.
     *
     * @return The decoded JSON string using UTF-8 charset
     */
    public String decode() {
        return URLDecoder.decode(json, StandardCharsets.UTF_8);
    }

    /**
     * Encodes the stored JSON string to URL encoding.
     *
     * @return The URL-encoded JSON string using UTF-8 charset
     */
    public String encode() {
        return URLEncoder.encode(json, StandardCharsets.UTF_8);
    }
}
