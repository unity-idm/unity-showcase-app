/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.imunity.cloud.showcase.subscription.Subscription;
import io.imunity.cloud.showcase.subscription.SubscriptionRepository;

@RestController
public class ShowcaseRestController
{
	private static String BEARER = "Bearer ";

	@Value("${rest.secret}")
	private String secret;

	@Autowired
	SubscriptionRepository subscriptionRepository;

	@GetMapping("/subscription")
	public void addSubscription(@RequestHeader("Authorization") String authHeader,
			@RequestParam(value = "tenantEntityId") String tenantEntityId,
			@RequestParam(value = "tenantDisplayedName") String tenantDisplayedName,
			@RequestParam(value = "ownerUserId") String ownerUserId)
	{

		verifyToken(authHeader);
		subscriptionRepository.save(new Subscription(tenantEntityId, tenantDisplayedName, ownerUserId));
	}

	private void verifyToken(String authHeader)
	{
		if (authHeader == null || authHeader.isEmpty() || !(BEARER + secret).equals(authHeader))
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for rest client");
		}
	}
}
