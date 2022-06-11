package ch.bader.budget.server.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("virtualAccount")
public class VirtualAccountDbo {

    @Id
    private String id;
    private String name;
    private BigDecimal balance;
    private Boolean isDeleted;
    private String underlyingAccountId;

}
