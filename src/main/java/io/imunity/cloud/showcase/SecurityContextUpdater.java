/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import io.imunity.cloud.showcase.rest.UnityRestClient;
import io.imunity.cloud.showcase.rest.UnityRestClient.SubscriptionDetails;
import io.imunity.cloud.showcase.rest.UnityRestClient.SubscriptionUser;

@Component
public class SecurityContextUpdater
{
	@Autowired
	private InvocationContext context;
	
	@Autowired
	private UnityRestClient unityRestClient;
	
	public void updateSecurityContext(Authentication authentication, String subscription)
	{
		SubscriptionDetails subscriptionFull = unityRestClient.getSubscriptionDetails(subscription);
		context.setSubscritpion(subscriptionFull);
		context.setUserId(getUserId(authentication));
		List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
		if (subscriptionFull.isPremium())
		{
			updatedAuthorities.add(new SimpleGrantedAuthority(SecurityConfig.PREMIUM_AUTHORITY));
		}else
		{
			updatedAuthorities.remove(new SimpleGrantedAuthority(SecurityConfig.PREMIUM_AUTHORITY));
		}

		Optional<SubscriptionUser> usr = unityRestClient.getUser(subscription, getUserId(authentication));
		if (usr.isPresent() && usr.get().isAdmin())
		{
			updatedAuthorities.add(new SimpleGrantedAuthority(SecurityConfig.ADMIN_AUTHORITY));
		}else
		{
			updatedAuthorities.remove(new SimpleGrantedAuthority(SecurityConfig.ADMIN_AUTHORITY));
		}
		
		if (subscriptionFull.getStatus().equals("ACTIVE"))
		{
			updatedAuthorities.add(new SimpleGrantedAuthority(SecurityConfig.ACTIVE_ACCOUNT_AUTHORITY));
		}else
		{
			updatedAuthorities.remove(new SimpleGrantedAuthority(SecurityConfig.ACTIVE_ACCOUNT_AUTHORITY));
		}
		
		OAuth2User principal = ((OAuth2AuthenticationToken) authentication).getPrincipal();
		Authentication newAuth = new OAuth2AuthenticationToken(principal, updatedAuthorities,
				(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId()));
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
	
	public static String getUserId(Authentication authentication)
	{
		return ((OAuth2AuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("sub");
	}

}
