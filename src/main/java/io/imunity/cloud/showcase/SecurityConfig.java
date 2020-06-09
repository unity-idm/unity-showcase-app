/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	public static final String PREMIUM_AUTHORITY = "PREMIUM";
	public static final String ADMIN_AUTHORITY = "ADMIN";
	public static final String ACTIVE_ACCOUNT_AUTHORITY = "ACTIVE_ACCOUNT";

	@Autowired
	private Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests(a -> a
				.antMatchers("/", "/favicon.ico", "/login", "/logout/**", "/pricing", "/error",
						"/webjars/**", "/access_denied", "/images/**", "/css/**", "/subscription")
				.permitAll().anyRequest().authenticated())
				.oauth2Login()
				
				.loginPage("/login")
				.successHandler(successHandler()).and().logout().logoutSuccessUrl("/login").and()
				.exceptionHandling().accessDeniedPage("/access_denied").and().csrf().disable().headers()
				.referrerPolicy(ReferrerPolicy.SAME_ORIGIN);

	}

	private AuthenticationSuccessHandler successHandler()
	{
		return new AuthenticationSuccessHandler()
		{
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException
			{
				new DefaultRedirectStrategy().sendRedirect(request, response, "/select-subscription");
			}
		};
	}
	
	@PostConstruct
	private void configureSSL()
	{
		System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
		System.setProperty("javax.net.ssl.trustStorePassword",
				env.getProperty("server.ssl.trust-store-password"));
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
		{
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		});
	}
}
