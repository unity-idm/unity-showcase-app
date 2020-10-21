/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity
{
	private EntityInformation entityInformation;

	@JsonCreator
	public Entity()
	{

	}

	public EntityInformation getEntityInformation()
	{
		return entityInformation;
	}

	public void setEntityInformation(EntityInformation entityInformation)
	{
		this.entityInformation = entityInformation;
	}
}
