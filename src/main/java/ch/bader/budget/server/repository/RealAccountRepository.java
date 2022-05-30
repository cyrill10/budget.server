package ch.bader.budget.server.repository;

import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;
import ch.bader.budget.server.adapter.sql.repository.RealAccountJpaRepository;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.RealAccountMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class RealAccountRepository {

    @Autowired
    private RealAccountMapper realAccountMapper;

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

//    @Autowired
//    private RealAccountRepositoryCosmos realAccountRepositoryCosmos;

    @Autowired
    private RealAccountJpaRepository realAccountJpaRepository;

    public RealAccount save(RealAccount realAccount) {
//        RealAccountDbo cosmosAccount = realAccountMapper.mapToEntity(
//                realAccount);
        RealAccountDboSql sqlAccount = realAccountMapper.mapToOldEntity(realAccount);
//        cosmosAccount = realAccountRepositoryCosmos.save(cosmosAccount);
        sqlAccount = realAccountJpaRepository.save(sqlAccount);
        return realAccountMapper.mapToDomain(sqlAccount);
    }

    public RealAccount getAccountById(Integer id) {
        RealAccountDboSql realAccountDboSql = realAccountJpaRepository.findById(id).orElseThrow();
        return realAccountMapper.mapToDomain(realAccountDboSql);
    }

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

    public RealAccount updateRealAccount(RealAccount account) {
        RealAccountDboSql sqlAccount = realAccountMapper.mapToOldEntity(account);
        sqlAccount.updateEnums();
        sqlAccount = realAccountJpaRepository.save(sqlAccount);
        return realAccountMapper.mapToDomain(sqlAccount);
    }
}
