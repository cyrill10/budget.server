package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.VirtualAccountDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.VirtualAccountJpaRepository;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("virtualAccountMySql")
public class VirtualAccountAdapterImpl implements VirtualAccountAdapter {

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

    @Autowired
    private VirtualAccountJpaRepository virtualAccountJpaRepository;


    @Override
    public VirtualAccount save(VirtualAccount virtualAccount) {
        VirtualAccountDboSql sqlAccount = virtualAccountMapper.mapToOldEntity(virtualAccount);
        sqlAccount = virtualAccountJpaRepository.save(sqlAccount);
        return virtualAccountMapper.mapToDomain(sqlAccount);
    }

    @Override
    public VirtualAccount updateVirtualAccount(VirtualAccount account) {
        VirtualAccountDboSql sqlAccount = virtualAccountMapper.mapToOldEntity(account);
        sqlAccount = virtualAccountJpaRepository.save(sqlAccount);
        return virtualAccountMapper.mapToDomain(sqlAccount);
    }

    @Override
    public VirtualAccount getAccountById(String id) {
        VirtualAccountDboSql virtualAccountDboSql = virtualAccountJpaRepository.findById(Integer.parseInt(id))
                                                                               .orElseThrow();
        return virtualAccountMapper.mapToDomain(virtualAccountDboSql);
    }

    @Override
    public List<VirtualAccount> getAllVirtualAccountsWithTheirUnderlyingAccount() {
        return virtualAccountJpaRepository.findAll().stream().map(virtualAccountMapper::mapToDomain).sorted().collect(
            Collectors.toList());
    }

    @Override
    public List<VirtualAccount> getAllVirtualAccountsForRealAccount(String realAccountId) {
        List<VirtualAccountDboSql> accounts = virtualAccountJpaRepository.findAllByUnderlyingAccountId(Integer.parseInt(
            realAccountId));
        return accounts.stream().map(virtualAccountMapper::mapToDomain).sorted().collect(Collectors.toList());
    }
}
