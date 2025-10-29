package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zw.co.hcpwebcommons.domain.converter.EmailConverter;
import zw.co.hcpwebcommons.domain.converter.UserNameConverter;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.UserName;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditTrail extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullName;

    private String userName;

    private String role;

    private String narration;
}