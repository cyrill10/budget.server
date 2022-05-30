package ch.bader.budget.server.integration;

import ch.bader.budget.server.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class OverviewIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturnOverview() throws Exception {
        // Given

        String mills2022May1 = "1651363200000";
        String expectedJson = TestUtils.loadFile("json/overview.json");
        System.out.println(expectedJson);

        // When & Then
        mockMvc.perform(get("/budget/overview/list")
                       .param("dateLong", mills2022May1)
                       .contentType(MediaType.APPLICATION_JSON)
                       .headers(getAuthHeader())
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(expectedJson));
    }
}
