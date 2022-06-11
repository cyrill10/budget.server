package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.VirtualAccountDbo;
import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;
import ch.bader.budget.server.adapter.sql.entity.VirtualAccountDboSql;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.type.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VirtualAccountMapperTest {

    @Autowired
    private VirtualAccountMapperImpl sut;

    @Test
    public void shouldMapRealAccountToDto() {
        //given
        RealAccount realAccount = RealAccount
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(AccountType.CHECKING)
            .build();

        VirtualAccount domain = VirtualAccount
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();

        //when
        VirtualAccountBoundaryDto account = sut.mapToDto(domain);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo("idVirtual");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccount().getId()).isEqualTo("id");
        assertThat(account.getUnderlyingAccount()
                          .getAccountType()
                          .getValue()).isEqualTo(AccountType.CHECKING.getValue());
    }

    @Test
    public void shouldMapDtoToRealAccount() {
        //given
        RealAccountBoundaryDto realAccountBoundaryDto = RealAccountBoundaryDto
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(ValueEnumDto.builder()
                                     .value(
                                         AccountType.CHECKING.getValue())
                                     .name(AccountType.CHECKING.getName())
                                     .build())
            .build();

        VirtualAccountBoundaryDto dto = VirtualAccountBoundaryDto
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccountBoundaryDto)
            .build();


        //when
        VirtualAccount account = sut.mapToDomain(dto);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo("idVirtual");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccount().getId()).isEqualTo("id");
        assertThat(account.getUnderlyingAccount()
                          .getAccountType()).isEqualTo(AccountType.CHECKING);
    }

    @Test
    public void shouldMapRealAccountToDbo() {
        //given
        RealAccount realAccount = RealAccount
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(AccountType.CHECKING)
            .build();

        VirtualAccount domain = VirtualAccount
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();

        //when
        VirtualAccountDbo account = sut.mapToEntity(domain);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo("idVirtual");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccountId()).isEqualTo("id");
    }

    @Test
    public void shouldMapDboToRealAccount() {
        //given
        VirtualAccountDbo dbo = VirtualAccountDbo
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccountId("id")
            .build();

        //when
        VirtualAccount account = sut.mapToDomain(dbo);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo("idVirtual");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccount()).isNull();
    }

    @Test
    public void shouldMapRealAccountToOldDbo() {
        //given
        RealAccount realAccount = RealAccount.builder()
                                             .id("1")
                                             .name("realAccountName")
                                             .accountType(AccountType.CHECKING)
                                             .build();

        VirtualAccount domain = VirtualAccount
            .builder()
            .id("3")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();


        //when
        VirtualAccountDboSql account = sut.mapToOldEntity(domain);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo(3);
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccount().getId()).isEqualTo(1);
        assertThat(account.getUnderlyingAccount()
                          .getAccountType()
                          .getValue()).isEqualTo(AccountType.CHECKING.getValue());
    }

    @Test
    public void shouldMapOldDboToRealAccount() {
        //given
        RealAccountDboSql realAccountDboSql = new RealAccountDboSql();
        realAccountDboSql.setId(1);
        realAccountDboSql.setName("realAccountName");
        realAccountDboSql.setAccountType(AccountType.CHECKING);

        VirtualAccountDboSql dbo = VirtualAccountDboSql
            .builder()
            .id(3)
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccountDboSql)
            .build();


        //when
        VirtualAccount account = sut.mapToDomain(dbo);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("virtualAccountName");
        assertThat(account.getId()).isEqualTo("3");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getIsDeleted()).isEqualTo(false);
        assertThat(account.getUnderlyingAccount().getId()).isEqualTo("1");
        assertThat(account.getUnderlyingAccount()
                          .getAccountType()).isEqualTo(AccountType.CHECKING);
    }
}