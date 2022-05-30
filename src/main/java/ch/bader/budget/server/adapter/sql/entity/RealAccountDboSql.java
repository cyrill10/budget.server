package ch.bader.budget.server.adapter.sql.entity;

import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "real_account")
public class RealAccountDboSql {

    @Id
    @SequenceGenerator(name = "real_account_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "real_account_seq")
    private Integer id;

    @Basic
    private int accountTypeValue;

    @Transient
    private AccountType accountType;

    @PostLoad
    void fillTransient() {
        if (accountTypeValue > 0) {
            this.accountType = EnumUtil.getEnumForValue(AccountType.class, accountTypeValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (accountType != null) {
            this.accountTypeValue = accountType.getValue();
        }
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    void fillUpdate() {
        updateDate = LocalDateTime.now();
    }

    @NonNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "underlyingAccount")
    @JsonIgnore
    private List<VirtualAccountDboSql> virtualAccounts;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public List<VirtualAccountDboSql> getVirtualAccounts() {
        return virtualAccounts;
    }


    public String toString() {
        return this.getName();
    }

    public void updateEnums() {
        fillPersistent();
    }

}
