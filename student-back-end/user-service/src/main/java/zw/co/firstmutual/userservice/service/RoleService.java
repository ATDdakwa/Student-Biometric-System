package zw.co.firstmutual.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.firstmutual.userservice.domain.model.Role;
import zw.co.firstmutual.userservice.domain.repository.RoleRepository;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.exceptions.RoleNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role add(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getOne(Long id) {
        var role = roleRepository.findById(id);
        if (role.isEmpty())
            throw new RoleNotFoundException("role.not.found", ExceptionCode.ROLE_NOT_FOUND);
        return role.get();
    }

    public Role findByName(String name) {
        var role = roleRepository.findByName(name);
        if (role == null)
            throw new RoleNotFoundException("role " + name + " not.found", ExceptionCode.ROLE_NOT_FOUND);

        return role;
    }
}