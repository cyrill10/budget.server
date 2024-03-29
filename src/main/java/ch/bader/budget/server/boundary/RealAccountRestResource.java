package ch.bader.budget.server.boundary;

import ch.bader.budget.server.boundary.dto.AccountElementBoundaryDto;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.core.account.real.RealAccountService;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.RealAccountMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.type.AccountType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/realAccount/")
public class RealAccountRestResource {

    private final RealAccountService realAccountService;

    private final RealAccountMapper realAccountMapper;

    private final VirtualAccountMapper virtualAccountMapper;

    public RealAccountRestResource(RealAccountService realAccountService, RealAccountMapper realAccountMapper,
                                   VirtualAccountMapper virtualAccountMapper) {
        this.realAccountService = realAccountService;
        this.realAccountMapper = realAccountMapper;
        this.virtualAccountMapper = virtualAccountMapper;
    }


    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public RealAccountBoundaryDto addNewAccount(@RequestBody RealAccountBoundaryDto accountDto) {
        RealAccount account = realAccountMapper.mapToDomain(accountDto);
        account = realAccountService.addRealAccount(account);
        return realAccountMapper.mapToDto(account);

    }

    @GetMapping(path = "/")
    public RealAccountBoundaryDto getAccount(@RequestParam String id) {
        RealAccount result = realAccountService.getAccountById(id);
        return realAccountMapper.mapToDto(result);
    }

    @GetMapping(path = "/list")
    public List<AccountElementBoundaryDto> getAllAccounts() {
        Map<RealAccount, List<VirtualAccount>> realAccounts = realAccountService.getAccountMap();
        return realAccounts.entrySet()
                           .stream()
                           .map(entry -> new AccountElementBoundaryDto(realAccountMapper.mapToDto(entry.getKey()),
                               entry.getValue().stream().map(virtualAccountMapper::mapToDto)
                                    .collect(Collectors.toList())))
                           .collect(Collectors.toList());
    }


    @PutMapping(path = "/update")
    public RealAccountBoundaryDto updateAccount(@RequestBody RealAccountBoundaryDto dto) {
        RealAccount account = realAccountMapper.mapToDomain(dto);
        account = realAccountService.updateRealAccount(account);
        return realAccountMapper.mapToDto(account);
    }

    @GetMapping(path = "/type/list")
    public List<AccountType> getAllAccountTypes() {
        return Arrays.asList(AccountType.values());
    }
}
