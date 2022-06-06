package ch.bader.budget.server.repository;

import ch.bader.budget.server.adapter.sql.entity.VirtualAccountDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.VirtualAccountJpaRepository;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VirtualAccountRepository {

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

    @Autowired
    private VirtualAccountJpaRepository virtualAccountRepository;


    public VirtualAccount save(VirtualAccount virtualAccount) {
        VirtualAccountDboSql sqlAccount = virtualAccountMapper.mapToOldEntity(virtualAccount);
        sqlAccount = virtualAccountRepository.save(sqlAccount);
        return virtualAccountMapper.mapToDomain(sqlAccount);
    }

    public VirtualAccount updateVirtualAccount(VirtualAccount account) {
        VirtualAccountDboSql sqlAccount = virtualAccountMapper.mapToOldEntity(account);
        sqlAccount = virtualAccountRepository.save(sqlAccount);
        return virtualAccountMapper.mapToDomain(sqlAccount);
    }

    public VirtualAccount getAccountById(Integer id) {
        VirtualAccountDboSql virtualAccountDboSql = virtualAccountRepository.findById(id).orElseThrow();
        return virtualAccountMapper.mapToDomain(virtualAccountDboSql);
    }

    public Optional<VirtualAccount> findByAccountId(String id) {
        Optional<VirtualAccountDboSql> virtualAccountDboSql = virtualAccountRepository.findById(Integer.parseInt(id));
        return virtualAccountDboSql.map(virtualAccountMapper::mapToDomain);
    }

    public List<VirtualAccount> getAllVirtualAccountsWithTheirUnderlyingAccount() {
        return virtualAccountRepository.findAll().stream().map(virtualAccountMapper::mapToDomain).sorted().collect(
                Collectors.toList());
    }

    public List<VirtualAccount> getAllVirtualAccountsForRealAccount(Integer realAccountId) {
        List<VirtualAccountDboSql> accounts = virtualAccountRepository.findAllByUnderlyingAccountId(realAccountId);
        return accounts.stream().map(virtualAccountMapper::mapToDomain).sorted().collect(Collectors.toList());
    }
}
