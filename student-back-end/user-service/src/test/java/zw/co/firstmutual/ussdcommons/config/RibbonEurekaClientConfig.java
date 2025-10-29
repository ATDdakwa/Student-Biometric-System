package zw.co.firstmutual.ussdcommons.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RibbonEurekaClientConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    public ServerList<Server> getServerList(IClientConfig config) {

        return new ServerList<Server>() {
            @Override
            public List<Server> getInitialListOfServers() {
                return this.getUpdatedListOfServers();
            }

            @Override
            public List<Server> getUpdatedListOfServers() {
                List<Server> serverList = new ArrayList<>();

                List<InstanceInfo> list = discoveryClient.getInstancesById(config.getClientName());
                for (InstanceInfo instance : list) {
                    serverList.add(new Server(instance.getHostName(), instance.getPort()));
                }
                return serverList;
            }
        };
    }
}
