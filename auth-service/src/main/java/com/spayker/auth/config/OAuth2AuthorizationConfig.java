package com.spayker.auth.config;

import com.spayker.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 *  OAuth2AuthorizationConfig class that implements AuthorizationServerConfigurerAdapter. Typically
 *  contains major configuration including secured access to certain services by certain client and scope (ui, server)
 **/
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private MongoUserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    /**
     *  Overrides configuration by declaring individual clients and their properties..
     *  @param clients - instance of ClientDetailsServiceConfigurer class
     *  @throws Exception instance
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password","client_credentials")
                .scopes("ui")
                .and()
                .withClient("account-service")
                .secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .and()
                .withClient("device-service")
                .secret(env.getProperty("DEVICE_SERVICE_PASSWORD"))
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server");
    }

    /**
     *  Configures endpoints by setting tokenStore, authenticationManager and userDetailsService.
     *  @param endpoints - instance of AuthorizationServerEndpointsConfigurer which must be updated with tokenStore,
     *                   authenticationManager and userDetailsService
     **/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userDetailsService);
    }

    /**
     *  Configures AuthorizationServerSecurityConfigurer object by setting tokenKeyAccess, checkTokenAccess and passwordEncoder.
     *  @param oauthServer - instance of AuthorizationServerSecurityConfigurer which must be updated with tokenKeyAccess,
     *                     checkTokenAccess and passwordEncoder
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
