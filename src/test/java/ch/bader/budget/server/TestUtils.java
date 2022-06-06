package ch.bader.budget.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestUtils {

    public static String loadFile(String fileName) throws IOException {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
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