package zw.co.hcpwebcommons.domain.value;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jackson Tungamira <tungamirajackson@gmail.com>
 */

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 6992256126169442749L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime dateCreated;

    @LastModifiedDate
    private LocalDateTime dateModified;

    @CreatedDate
    private LocalDateTime request_date;

    @Version
    private Long version;

    private Boolean active = Boolean.TRUE;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        return this.getId().equals(((BaseEntity) obj).getId());
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}