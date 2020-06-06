/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest.types;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.imunity.cloud.showcase.Constans;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantUser
{
	private String id;
	private Map<String, Attribute> attributesByName;

	@JsonCreator
	public TenantUser()
	{
	}

	@JsonIgnore
	public boolean isAdmin()
	{
		Attribute attr = attributesByName.get(Constans.SUBSCRIPTION_APP_ROLE);
		if (attr != null && attr.values.size() > 0 && attr.values.get(0).toLowerCase().equals("admin"))
			return true;
		return false;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Map<String, Attribute> getAttributesByName()
	{
		return attributesByName;
	}

	public void setAttributesByName(Map<String, Attribute> attributesByName)
	{
		this.attributesByName = attributesByName;
	}

	@JsonIgnore
	public String getEmail()
	{
		Attribute attr = attributesByName.get(Constans.EMAIL_ATTR);
		if (attr != null && attr.values.size() > 0)
			try
			{
				return Constans.MAPPER.readValue(attr.values.get(0), ObjectNode.class).get("value")
						.asText();
			} catch (JsonProcessingException e)
			{
				return "";
			}
		return "";
	}

	@JsonIgnore
	public String getFirstname()
	{
		Attribute attr = attributesByName.get(Constans.FIRSTNAME_ATTR);
		if (attr != null && attr.values.size() > 0)
		{
			return attr.values.get(0);
		}
		return "";
	}

	@JsonIgnore
	public String getSurname()
	{
		Attribute attr = attributesByName.get(Constans.SURNAME_ATTR);
		if (attr != null && attr.values.size() > 0)
		{
			return attr.values.get(0);
		}
		return "";
	}

	@JsonIgnore
	public String getName()
	{
		Attribute attr = attributesByName.get(Constans.NAME_ATTR);
		if (attr != null && attr.values.size() > 0)
		{
			return attr.values.get(0);
		}
		return "";
	}
}