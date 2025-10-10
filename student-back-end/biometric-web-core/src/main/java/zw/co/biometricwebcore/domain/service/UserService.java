package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.biometricwebcore.domain.model.User;
import zw.co.biometricwebcore.feign.UserFeignClient;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserFeignClient userFeignClient;

    public User findByEmail(Email email) {
        return userFeignClient.findByEmail(email);
    }
}