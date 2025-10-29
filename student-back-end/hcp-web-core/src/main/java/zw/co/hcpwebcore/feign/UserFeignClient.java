package zw.co.hcpwebcore.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcore.domain.model.User;

@FeignClient(name = "USER-SERVICE", decode404 = true, url = "${spring.feign.user.client}")
public interface UserFeignClient {

    @GetMapping("/api/v1/users/email")
    User findByEmail(@RequestParam("emailAddress") Email email);
}
