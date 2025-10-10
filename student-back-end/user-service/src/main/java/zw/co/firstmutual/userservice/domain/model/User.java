package zw.co.firstmutual.userservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zw.co.hcpwebcommons.domain.converter.EmailConverter;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;
import zw.co.hcpwebcommons.domain.value.Email;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dashboard_users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

//    @Column(unique = true)
//    @Convert(converter = UserNameConverter.class)
    private String userName;

    @Column(unique = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    private String userEmail;

    private String password;

    private String sbu;

    private String userRole;

    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> role;
}