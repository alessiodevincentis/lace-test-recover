package com.woonders.lacemsapi.config;

import com.woonders.lacemscommon.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Created by ema98 on 8/10/2017.
 */
//https://github.com/wizardkyn/BootRestOAuth2
@Configuration
public class OAuth2ServerConfigJwt {

    private static final String RESOURCE_ID = "REST_SERVICE";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(final HttpSecurity http) throws Exception {

            //una volta specificati, se poi si fa come sotto dove diciamo che ogni request deve essere autenticata, ormai questi sono
            //stati specificati e rimangono cosi. Se si specificano dopo invece non funzionano, serve l-autenticazione
            http.authorizeRequests().antMatchers("/v1/lacepushlead").permitAll()
                    .antMatchers("/lacepushlead").permitAll()
                    .antMatchers("/v1/lacepushlead/json").permitAll()
                    .antMatchers("/lacepushlead/json").permitAll()
                    .antMatchers("/v1/pushlead").permitAll()
                    .antMatchers("/pushlead").permitAll()
                    .antMatchers("/v1/pushsmsdeliveryreceipt").permitAll()
                    .antMatchers("/pushsmsdeliveryreceipt").permitAll()
                    .antMatchers("/v1/esito-contestazione").permitAll()
                    .antMatchers("/esito-contestazione").permitAll()
                    .antMatchers("/v1/esito-contestazione2").permitAll()
                    .antMatchers("/esito-contestazione2").permitAll();

            http.authorizeRequests()
                    .anyRequest().authenticated();

        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private OperatorService operatorService;

        //dice che non lo trova ma in realta lo trova
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            return new JwtAccessTokenConverter();
        }

        @Override
        public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .accessTokenConverter(accessTokenConverter())
                    .userDetailsService(operatorService)
                    .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("rest-client")
                    .resourceIds(RESOURCE_ID)
                    .authorizedGrantTypes("password")
                    .authorities("USER")
                    .scopes("read", "write", "trust")
                    .secret("rest-secret");
        }
    }
}
