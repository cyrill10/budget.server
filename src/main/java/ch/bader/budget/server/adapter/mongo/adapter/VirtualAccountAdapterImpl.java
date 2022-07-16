package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.VirtualAccountDbo;
import ch.bader.budget.server.adapter.mongo.repository.VirtualAccountMongoRepository;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.RealAccountAdapter;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("virtualAccountMongo")
public class VirtualAccountAdapterImpl implements VirtualAccountAdapter {

    @Autowired
    private VirtualAccountMapper virtualAccountMapper;

    @Autowired
    private VirtualAccountMongoRepository virtualAccountMongoRepository;

    @Autowired
    @Qualifier("realAccountMongo")
    private RealAccountAdapter realAccountAdapter;


    @Override
    public VirtualAccount save(VirtualAccount virtualAccount) {
        VirtualAccountDbo accountDbo = virtualAccountMapper.mapToEntity(virtualAccount);
        accountDbo = virtualAccountMongoRepository.save(accountDbo);
        VirtualAccount virtualAccountSaved = virtualAccountMapper.mapToDomain(accountDbo);
        virtualAccountSaved.setUnderlyingAccount(virtualAccount.getUnderlyingAccount());
        return virtualAccountSaved;
    }

    @Override
    public VirtualAccount updateVirtualAccount(VirtualAccount virtualAccount) {
        VirtualAccountDbo accountDbo = virtualAccountMapper.mapToEntity(virtualAccount);
        accountDbo = virtualAccountMongoRepository.save(accountDbo);
        VirtualAccount virtualAccountSaved = virtualAccountMapper.mapToDomain(accountDbo);
        virtualAccountSaved.setUnderlyingAccount(virtualAccount.getUnderlyingAccount());
        return virtualAccountSaved;
    }

    @Override
    public VirtualAccount getAccountById(String id) {
        VirtualAccountDbo accountDbo = virtualAccountMongoRepository
            .findById(id)
            .orElseThrow();
        VirtualAccount virtualAccount = virtualAccountMapper.mapToDomain(accountDbo);
        return addRealAccountToVirtualAccount(virtualAccount, accountDbo.getUnderlyingAccountId());

    }

    @Override
    public List<VirtualAccount> getAllVirtualAccountsWithTheirUnderlyingAccount() {
        List<VirtualAccountDbo> virtualAccountDbos = virtualAccountMongoRepository
            .findAll();
        List<RealAccount> realAccounts = realAccountAdapter.
            findAll();

        return virtualAccountDbos.stream().map(vadbo -> {
            RealAccount ra = realAccounts
                .stream()
                .filter(a -> a.getId().equals(vadbo.getUnderlyingAccountId()))
                .findFirst()
                .orElseThrow();

            VirtualAccount va = virtualAccountMapper.mapToDomain(vadbo);
            va.setUnderlyingAccount(ra);
            return va;
        }).sorted().collect(Collectors.toList());

    }

    @Override
    public List<VirtualAccount> getAllVirtualAccountsForRealAccount(String realAccountId) {
        List<VirtualAccountDbo> accounts = virtualAccountMongoRepository.findAllByUnderlyingAccountId(realAccountId);
        RealAccount realAccount = realAccountAdapter.getAccountById(realAccountId);
        return accounts.stream().map(va -> {
            VirtualAccount virtualAccount = virtualAccountMapper.mapToDomain(va);
            virtualAccount.setUnderlyingAccount(realAccount);
            return virtualAccount;
        }).sorted().collect(Collectors.toList());
    }

    private VirtualAccount addRealAccountToVirtualAccount(VirtualAccount virtualAccount, String realAccountId) {
        virtualAccount.setUnderlyingAccount(realAccountAdapter.getAccountById(realAccountId));
        return virtualAccount;
    }
}
