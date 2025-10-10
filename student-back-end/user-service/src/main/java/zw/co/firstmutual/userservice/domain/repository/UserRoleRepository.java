package zw.co.firstmutual.userservice.domain.repository;

import org.springframework.stereotype.Repository;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;

@Repository
public interface UserRoleRepository extends CustomRepository<User, Long> {
}
