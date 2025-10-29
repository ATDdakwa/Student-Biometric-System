package zw.co.firstmutual.userservice.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zw.co.firstmutual.userservice.domain.model.UserActivity;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;

import java.util.List;

@Repository
public interface ActivityRepository extends CustomRepository<UserActivity, Long> {

    @Query("SELECT a from UserActivity a WHERE a.entityId = ?1")
    List<UserActivity> getAllByEntityId(Long entityId);
}