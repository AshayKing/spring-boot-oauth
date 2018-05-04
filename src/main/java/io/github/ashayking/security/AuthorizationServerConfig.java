package io.github.ashayking.security;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 
 * @author Ashay S Patil
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${resource.id:spring-boot-application}")
	private String resourceId;
	
    @Value("${access_token.validity_period:3600}")
    private Integer accessTokenValiditySeconds = 3600;
    
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenServices(tokenServices()).tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
				.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			   .withClient("normal-app")
			   		.authorizedGrantTypes("authorization_code","implicit")
			   		.authorities("ROLE_CLIENT")
			   		.scopes("read","write")
			   		.resourceIds(resourceId)
			   		.accessTokenValiditySeconds(accessTokenValiditySeconds)
			   		.refreshTokenValiditySeconds(10000)
			   		.and()
			   	.withClient("trusted-app")
			   		.authorizedGrantTypes("client_credentials", "password", "refresh_token")
			   		.authorities("ROLE_TRUSTED_CLIENT")
			   		.scopes("read","write")
			   		.resourceIds(resourceId)
			   		.accessTokenValiditySeconds(accessTokenValiditySeconds)
                    .refreshTokenValiditySeconds(10000)
                    .secret("secret");
	}

	@Autowired
	private SecretKeyProvider keyProvider;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		//converter.setSigningKey("123");
		
		try {
			converter.setSigningKey(keyProvider.getKey()); 
		} catch (KeyStoreException | NoSuchAlgorithmException | IOException
				| UnrecoverableKeyException | CertificateException e) {
			e.printStackTrace();
		}
		return converter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices =new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
		return defaultTokenServices;
	}

}