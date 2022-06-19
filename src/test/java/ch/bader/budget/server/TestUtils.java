package ch.bader.budget.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpHeaders;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestUtils {

    public static String loadFileAsString(String fileName) throws IOException {
        File file = loadFile(fileName);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static File loadFile(String fileName) {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }

    public static String asJsonString(final Object obj) {
        JavaTimeModule module = new JavaTimeModule();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpHeaders getAuthHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("user", "password");
        return header;
    }
}
