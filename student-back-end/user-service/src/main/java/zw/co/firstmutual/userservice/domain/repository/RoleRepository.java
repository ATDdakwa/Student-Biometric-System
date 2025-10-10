package zw.co.firstmutual.userservice.domain.repository;

import zw.co.firstmutual.userservice.domain.model.Role;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;

public interface RoleRepository extends CustomRepository<Role, Long> {
    Role findByName(String name);
}