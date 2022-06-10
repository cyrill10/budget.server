package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.RealAccountJpaRepository;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.RealAccountMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.RealAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component("realAccountMySql")
public class RealAccountAdapterImpl implements RealAccountAdapter {

    @Autowired
    private RealAccountMapper realAccountMapper;

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

    @Autowired
    private RealAccountJpaRepository realAccountJpaRepository;

    @Override
    public RealAccount save(RealAccount realAccount) {
        RealAccountDboSql sqlAccount = realAccountMapper.mapToOldEntity(realAccount);
        sqlAccount = realAccountJpaRepository.save(sqlAccount);
        return realAccountMapper.mapToDomain(sqlAccount);
    }

    @Override
    public RealAccount getAccountById(String id) {
        Integer idString = Integer.parseInt(id);
        RealAccountDboSql realAccountDboSql = realAccountJpaRepository.findById(idString).orElseThrow();
        return realAccountMapper.mapToDomain(realAccountDboSql);
    }

    @Override
    public Map<RealAccount, List<VirtualAccount>> getAccountMap() {
        List<RealAccountDboSql> accounts = realAccountJpaRepository.findAll();
        return accounts.stream()
                       .collect(Collectors.toMap(a -> realAccountMapper.mapToDomain(a),
                           a -> a.getVirtualAccounts()
                                 .stream()
                                 .map(virtualAccountMapper::mapToDomain)
                                 .sorted()
                                 .collect(
                                     Collectors.toList()), (a, b) -> a, TreeMap::new));
    }

    @Override
    public RealAccount updateRealAccount(RealAccount account) {
        RealAccountDboSql sqlAccount = realAccountMapper.mapToOldEntity(account);
        sqlAccount.updateEnums();
        sqlAccount = realAccountJpaRepository.save(sqlAccount);
        sqlAccount.reloadEnums();
        return realAccountMapper.mapToDomain(sqlAccount);
    }
}
