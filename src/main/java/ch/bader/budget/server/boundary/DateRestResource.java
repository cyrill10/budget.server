package ch.bader.budget.server.boundary;

import ch.bader.budget.server.boundary.time.MonthGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/budget/date/")
public class DateRestResource {


    @GetMapping(path = "/month/list")
    public List<LocalDate> getAllMonths() {
        return MonthGenerator.getallMonths();
    }
}
