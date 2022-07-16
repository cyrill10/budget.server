package ch.bader.budget.server.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("realAccount")
public class RealAccountDbo {

    @Id
    private String id;
    private String name;
    private ValueEnumDbo accountType;

}
