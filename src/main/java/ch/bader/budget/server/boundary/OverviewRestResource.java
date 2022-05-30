package ch.bader.budget.server.boundary;

import ch.bader.budget.server.boundary.dto.OverviewElementBoundaryDto;
import ch.bader.budget.server.core.overview.OverviewService;
import ch.bader.budget.server.mapper.OverviewElementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/overview/")
public class OverviewRestResource {

    @Autowired
    private OverviewElementMapper overviewElementMapper;

    @Autowired
    private OverviewService overviewService;

    @GetMapping(path = "/list")
    public List<OverviewElementBoundaryDto> getAllTransactions(@RequestParam long dateLong) {
        LocalDate localDate = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate();
        return overviewService.getAllTransactions(localDate)
                              .stream()
                              .map(overviewElementMapper::mapToDto)
                              .collect(Collectors.toList());
    }
}
