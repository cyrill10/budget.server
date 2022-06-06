package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.time.MonthGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class DateIntegrationTest2 extends AbstractSqlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturnMonths() throws Exception {
        // Given

        LocalDate firstDay = MonthGenerator.STARTDATE;
        LocalDate expectedLastDate = LocalDate.now().plusYears(1).withDayOfMonth(1);

        long diff = ChronoUnit.MONTHS.between(
            firstDay,
            expectedLastDate);
        // When & Then
        mockMvc.perform(get("/budget/date/month/list")
                   .contentType(MediaType.APPLICATION_JSON)
                   .headers(getAuthHeader())
                   .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.[0]").value(firstDay.toString()))
               .andExpect(jsonPath("$.size()").value(diff));
    }
}
