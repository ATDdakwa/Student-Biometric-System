package zw.co.hcpwebcommons.config;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LocalRibbonClientConfiguration {

    @Bean
    public ServerList<Server> ribbonServerList() {
        StaticServerList<Server> staticServerList = new StaticServerList<>((new Server("localhost", 8101))
                , new Server("localhost", 8102));
        return staticServerList;
    }
}