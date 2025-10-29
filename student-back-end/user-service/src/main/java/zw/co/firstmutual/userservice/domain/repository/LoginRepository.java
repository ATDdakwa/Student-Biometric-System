package zw.co.firstmutual.userservice.domain.repository;

import org.springframework.stereotype.Repository;
import zw.co.firstmutual.userservice.domain.model.Login;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoginRepository extends CustomRepository<Login, Long> {
    List<Login> findAllByUser(User user);

    List<Login> findAllByCreatedDate(Date date);
}
