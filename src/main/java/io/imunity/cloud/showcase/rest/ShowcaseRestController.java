/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.imunity.cloud.showcase.subscription.Subscription;
import io.imunity.cloud.showcase.subscription.SubscriptionRepository;

@RestController
public class ShowcaseRestController
{
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	
	@GetMapping("/subscription")
	public void addSubscription(@RequestParam(value = "tenantEntityId") String tenantEntityId, 
			@RequestParam(value = "tenantDisplayedName") String tenantDisplayedName, 
			@RequestParam(value = "ownerUserId") String ownerUserId)
	{

		subscriptionRepository.save(new Subscription(tenantEntityId, tenantDisplayedName, ownerUserId));
	}
}
