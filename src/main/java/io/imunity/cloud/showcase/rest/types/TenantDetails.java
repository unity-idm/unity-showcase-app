/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest.types;

import io.imunity.cloud.showcase.Constans;

public class TenantDetails
{
	public final Tenant tenant;
	public final TenantSubscription details;
	
	public TenantDetails(Tenant tenant, TenantSubscription subscription)
	{
		this.tenant = tenant;
		this.details = subscription;
	}
	
	public boolean isPremium()
	{
		Attribute role = tenant.attributesByName.get(Constans.TENANT_PLAN_ATTR);
		return role != null && role.getValues().size() > 0
				&& role.getValues().get(0).toLowerCase().equals("premium");
	}
	
	
	public TenantSubscription getDetails()
	{
		return details;
	}
	
	public Tenant getTenant()
	{
		return tenant;
	}
}