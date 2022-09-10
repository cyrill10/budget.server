package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import ch.bader.budget.server.adapter.mongo.entity.VirtualAccountDbo;
import ch.bader.budget.server.adapter.mongo.repository.RealAccountMongoRepository;
import ch.bader.budget.server.adapter.mongo.repository.VirtualAccountMongoRepository;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.AccountTypeMapper;
import ch.bader.budget.server.mapper.RealAccountMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.RealAccountAdapter;
import ch.bader.budget.server.type.AccountType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("realAccountMongo")
public class RealAccountAdapterImpl implements RealAccountAdapter {

    private final RealAccountMapper realAccountMapper;

    private final VirtualAccountMapper virtualAccountMapper;

    private final RealAccountMongoRepository realAccountMongoRepository;

    private final VirtualAccountMongoRepository virtualAccountMongoRepository;

    private final AccountTypeMapper accountTypeMapper;

    public RealAccountAdapterImpl(RealAccountMapper realAccountMapper, VirtualAccountMapper virtualAccountMapper,
                                  RealAccountMongoRepository realAccountMongoRepository,
                                  VirtualAccountMongoRepository virtualAccountMongoRepository,
                                  AccountTypeMapper accountTypeMapper) {
        this.realAccountMapper = realAccountMapper;
        this.virtualAccountMapper = virtualAccountMapper;
        this.realAccountMongoRepository = realAccountMongoRepository;
        this.virtualAccountMongoRepository = virtualAccountMongoRepository;
        this.accountTypeMapper = accountTypeMapper;
    }

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

    @Override
    public List<RealAccount> findAll() {
        return realAccountMongoRepository
            .findAll()
            .stream()
            .map(realAccountMapper::mapToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RealAccount> getAccountsByTyp(AccountType accountType) {
        return realAccountMongoRepository
            .findAllByAccountType(accountTypeMapper.mapToDbo(accountType))
            .stream()
            .map(realAccountMapper::mapToDomain).collect(Collectors.toList());
    }
}
