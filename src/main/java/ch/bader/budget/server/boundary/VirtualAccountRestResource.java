package ch.bader.budget.server.boundary;

import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.core.virtualAccount.VirtualAccountService;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/virtualAccount/")
public class VirtualAccountRestResource {

    @Autowired
    private VirtualAccountService virtualAccountService;

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;


    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public VirtualAccountBoundaryDto addNewAccount(@RequestBody VirtualAccountBoundaryDto dto) {
        VirtualAccount virtualAccount = virtualAccountMapper.mapToDomain(dto);
        virtualAccount.setBalance(BigDecimal.ZERO);
        virtualAccount.setIsDeleted(Boolean.FALSE);
        virtualAccount = virtualAccountService.updateVirtualAccount(virtualAccount);
        return virtualAccountMapper.mapToDto(virtualAccount);
    }

    @PutMapping(path = "/update")
    public VirtualAccountBoundaryDto updateAccount(@RequestBody VirtualAccountBoundaryDto dto) {
        VirtualAccount virtualAccount = virtualAccountMapper.mapToDomain(dto);
        virtualAccount = virtualAccountService.updateVirtualAccount(virtualAccount);
        return virtualAccountMapper.mapToDto(virtualAccount);
    }

    @GetMapping(path = "/")
    public VirtualAccountBoundaryDto getAccountById(@RequestParam String id) {
        VirtualAccount virtualAccount = virtualAccountService.getAccountById(id);
        return virtualAccountMapper.mapToDto(virtualAccount);
    }

    @GetMapping(path = "/list")
    public List<VirtualAccountBoundaryDto> getAllAccounts() {
        List<VirtualAccount> accounts = virtualAccountService.getAllVirtualAccounts();
        return accounts.stream().map(virtualAccountMapper::mapToDto).collect(Collectors.toList());
    }
}
