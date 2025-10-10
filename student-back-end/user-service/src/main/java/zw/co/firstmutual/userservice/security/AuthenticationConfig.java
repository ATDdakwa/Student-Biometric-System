//package zw.co.firstmutual.userservice.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.ldap.core.support.BaseLdapPathContextSource;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.ldap.authentication.BindAuthenticator;
//import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
//import org.springframework.security.ldap.authentication.LdapAuthenticator;
//import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
//
//@Configuration
//@RequiredArgsConstructor
//public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {
//
//    private final Environment env;
//
//
//
//    @Override
//    public void init(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth
//                .ldapAuthentication()
//                .userSearchFilter("(sAMAccountName=Domain Users)")
//                .userSearchBase("CN=Domain Users,CN=Users,DC=firstmutual,DC=co,DC=zw")
//                .userDnPatterns("CN={0},OU=GSS_ICT,OU=_First Mutual Health Company,DC=firstmutual,DC=co,DC=zw")
//                .groupSearchBase("CN=Domain Users,CN=Users,DC=firstmutual,DC=co,DC=zw")
//                .groupSearchFilter("member={0}")
//                .contextSource()
//                .url(env.getProperty("spring.ldap.urls"))
////                .ldif(env.getProperty("spring.ldap.embedded.ldif"))
//                .managerDn("CN={0},OU=GSS_ICT,OU=_First Mutual Health Company,DC=firstmutual,DC=co,DC=zw")
//                .managerPassword("password");
////                .and()
////                .passwordCompare()
////                .passwordEncoder(new BCryptPasswordEncoder())
////                .passwordAttribute("userPassword");
//
////            ActiveDirectoryLdapAuthenticationProvider provider=
////                    new ActiveDirectoryLdapAuthenticationProvider("firstmutual.co.zw"
////                            ,env.getProperty("spring.ldap.urls")
////                            ,"CN=Configuration,DC=firstmutual,DC=co,DC=zw");
////            auth.authenticationProvider(provider);
//
//    }
//
//
//
//    @Bean
//    public ActiveDirectoryLdapAuthenticationProvider adProvider() {
//        return new ActiveDirectoryLdapAuthenticationProvider("firstmutual.co.zw", env.getProperty("spring.ldap.urls"));
//    }
//
//    @Bean
//    BindAuthenticator authenticator(BaseLdapPathContextSource contextSource) {
//        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
//        authenticator.setUserDnPatterns(new String[] { "cn={0},ou=GSS_ICT" });
//        return authenticator;
//    }
//
//    @Bean
//    LdapAuthenticationProvider authenticationProvider(LdapAuthenticator authenticator) {
//        return new LdapAuthenticationProvider(authenticator);
//    }
//}
