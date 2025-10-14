package zw.co.biometricwebcore;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import zw.co.hcpwebcommons.config.FeignClientSecurityConfig;
import zw.co.hcpwebcommons.config.LocalRibbonClientConfiguration;
import zw.co.hcpwebcommons.security.JwtAuthenticationEntryPoint;


@SpringBootApplication(scanBasePackages = "zw.co")
@EnableDiscoveryClient
@EnableFeignClients("zw.co")
@Import({FeignClientSecurityConfig.class})
@RibbonClient(name = "BIOMETRIC-WEB-CORE", configuration = LocalRibbonClientConfiguration.class)
@EntityScan(basePackages = "zw.co.biometricwebcore.domain.model")
@EnableScheduling


public class BiometricWebCoreApplication {

    public static void main(String[] args) {

        System.setProperty("java.io.tmpdir", "C:\\Users\\dakwa\\AppData\\Local\\Temp\\Morfin_AuthB");

        SpringApplication.run(BiometricWebCoreApplication.class, args);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}