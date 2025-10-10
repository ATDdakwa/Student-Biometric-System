package zw.co.firstmutual.userservice.domain.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.domain.value.UserName;

import java.util.Optional;

@Repository
public interface UserRepository extends CustomRepository<User, Long> {

    User findUserByUserName(@Param("userName") String userName);

    Optional<User> findUserByEmail(@Param("email") Email email);

}