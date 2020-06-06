/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import io.imunity.cloud.showcase.rest.types.TenantDetails;

@Component
@SessionScope
public class InvocationContext
{
	private TenantDetails subscription;
	private String userId;
	
	public InvocationContext()
	{
		
	}

	public TenantDetails getSubscription()
	{
		return subscription;
	}

	public void setSubscription(TenantDetails subscritpion)
	{
		this.subscription = subscritpion;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}	
	
	public String getSubscriptionId()
	{
		return subscription.tenant.id;
	}
}
