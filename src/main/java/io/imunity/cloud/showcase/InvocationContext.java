/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import io.imunity.cloud.showcase.rest.UnityRestClient.SubscriptionDetails;

@Component
@SessionScope
public class InvocationContext
{
	private SubscriptionDetails subscritpion;
	private String userId;
	
	public InvocationContext()
	{
		
	}

	public SubscriptionDetails getSubscritpion()
	{
		return subscritpion;
	}

	public void setSubscritpion(SubscriptionDetails subscritpion)
	{
		this.subscritpion = subscritpion;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}	
}
