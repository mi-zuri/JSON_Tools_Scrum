package pl.put.poznan.transformer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * Main application class for the JSON Tools application.
 * This Spring Boot application provides REST endpoints for JSON transformation operations.
 * The application scans for components in the pl.put.poznan.transformer.rest package.
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.transformer.rest"})
public class JsonToolsApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(JsonToolsApplication.class, args);
    }
}
