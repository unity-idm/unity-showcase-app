/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.subscription;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Subscription
{
	@Id
	private String id;
	private String ownerId;
	private String name;

	public Subscription(String id, String name, String ownerId)
	{
		this.id = id;
		this.ownerId = ownerId;
		this.name = name;
	}

	public Subscription()
	{

	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

}
