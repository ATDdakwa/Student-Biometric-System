package zw.co.biometricwebcore.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.MobileNumber;

import javax.xml.registry.infomodel.User;


@FeignClient(name = "USER-SERVICE", decode404 = true, url = "${spring.feign.user.client}")
public interface UserFeignClient {

    @GetMapping("/api/v1/users/email")
    zw.co.biometricwebcore.domain.model.User findByEmail(@RequestParam("emailAddress") Email email);

    @GetMapping("/api/v1/users/mobile_number")
    User findMemberByMsisdn(@RequestParam("mobileNumber") MobileNumber mobileNumber);
}
