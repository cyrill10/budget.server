package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import ch.bader.budget.server.adapter.mongo.entity.VirtualAccountDbo;
import ch.bader.budget.server.adapter.mongo.repository.RealAccountMongoRepository;
import ch.bader.budget.server.adapter.mongo.repository.VirtualAccountMongoRepository;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.RealAccountMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.RealAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("realAccountMongo")
public class RealAccountAdapterImpl implements RealAccountAdapter {

    @Autowired
    private RealAccountMapper realAccountMapper;

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

    @Autowired
    private RealAccountMongoRepository realAccountMongoRepository;

    @Autowired
    VirtualAccountMongoRepository virtualAccountMongoRepository;

    @Override
    public RealAccount save(RealAccount realAccount) {
        RealAccountDbo realAccountDbo = realAccountMapper.mapToEntity(
            realAccount);
        realAccountDbo = realAccountMongoRepository.save(realAccountDbo);
        return realAccountMapper.mapToDomain(realAccountDbo);
    }

    @Override
    public RealAccount getAccountById(String id) {
        RealAccountDbo realAccountDbo = realAccountMongoRepository.findById(id).orElseThrow();
        return realAccountMapper.mapToDomain(realAccountDbo);
    }

    @Override
    public Map<RealAccount, List<VirtualAccount>> getAccountMap() {
        List<RealAccountDbo> realAccountDbos = realAccountMongoRepository.findAll();

        List<VirtualAccountDbo> virtualAccountDbos = virtualAccountMongoRepository.findAll();

        return realAccountDbos
            .stream()
            .map(realAccountMapper::mapToDomain)
            .collect(Collectors.toMap(Function.identity(), ra ->
                virtualAccountDbos.stream().filter(va -> va.getUnderlyingAccountId().equals(ra.getId())).map((va -> {
                    VirtualAccount virtualAccount = virtualAccountMapper.mapToDomain(va);
                    virtualAccount.setUnderlyingAccount(ra);
                    return virtualAccount;
                })).sorted().collect(Collectors.toList()), (a, b) -> a, TreeMap::new)
            );
    }

    @Override
    public RealAccount updateRealAccount(RealAccount account) {
        RealAccountDbo realAccountDbo = realAccountMapper.mapToEntity(account);
        realAccountDbo = realAccountMongoRepository.save(realAccountDbo);
        return realAccountMapper.mapToDomain(realAccountDbo);
    }
}
