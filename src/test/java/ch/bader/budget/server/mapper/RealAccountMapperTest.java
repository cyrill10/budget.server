package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.type.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RealAccountMapperTest {

    @Autowired
    private RealAccountMapperImpl sut;

    @Test
    void shouldMapRealAccountToDto() {
        //given
        RealAccount domain = RealAccount.builder()
                                        .id("id")
                                        .name("realAccountName")
                                        .accountType(AccountType.CHECKING)
                                        .build();

        //when
        RealAccountBoundaryDto account = sut.mapToDto(domain);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("realAccountName");
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getAccountType().getValue()).isEqualTo(AccountType.CHECKING.getValue());
    }

    @Test
    void shouldMapDtoToRealAccount() {
        //given
        RealAccountBoundaryDto dto = RealAccountBoundaryDto.builder()
                                                           .id("id")
                                                           .name("realAccountName")
                                                           .accountType(ValueEnumDto.builder()
                                                                                    .value(AccountType.CHECKING.getValue())
                                                                                    .name(AccountType.CHECKING.getName())
                                                                                    .build())
                                                           .build();

        //when
        RealAccount account = sut.mapToDomain(dto);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("realAccountName");
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getAccountType()).isEqualTo(AccountType.CHECKING);
    }

    @Test
    void shouldMapRealAccountToDbo() {
        //given
        RealAccount domain = RealAccount.builder()
                                        .id("id")
                                        .name("realAccountName")
                                        .accountType(AccountType.CHECKING)
                                        .build();

        //when
        RealAccountDbo account = sut.mapToEntity(domain);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("realAccountName");
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getAccountType().getValue()).isEqualTo(AccountType.CHECKING.getValue());
    }

    @Test
    void shouldMapDboToRealAccount() {
        //given
        RealAccountDbo dbo = RealAccountDbo.builder()
                                           .id("id")
                                           .name("realAccountName")
                                           .accountType(ValueEnumDbo.builder()
                                                                    .value(AccountType.CHECKING.getValue())
                                                                    .name(AccountType.CHECKING.getName())
                                                                    .build())
                                           .build();

        //when
        RealAccount account = sut.mapToDomain(dbo);

        //then
        assertThat(account).isNotNull();
        assertThat(account.getName()).isEqualTo("realAccountName");
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getAccountType()).isEqualTo(AccountType.CHECKING);
    }
}