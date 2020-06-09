/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import io.imunity.cloud.showcase.rest.types.TenantDetails;
import io.imunity.cloud.showcase.rest.types.TenantUser;

@Component
@SessionScope
public class InvocationContext
{
	private TenantDetails subscription;
	private TenantUser user;
	
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

	public TenantUser getUser()
	{
		return user;
	}

	public void setUser(TenantUser user)
	{
		this.user = user;
	}	
	
	public String getSubscriptionId()
	{
		return subscription.tenant.id;
	}
	
	public String getUserId()
	{
		return user.getId();
	}
}
