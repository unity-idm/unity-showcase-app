/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest.types;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.imunity.cloud.showcase.Constans;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenant
{
	public final String id;
	public final String creatorId;
	public final String group;
	public final Map<String, Attribute> attributesByName;

	@JsonCreator
	public Tenant(@JsonProperty("id") String id, @JsonProperty("creatorId") String creatorId,
			@JsonProperty("group") String group,
			@JsonProperty("attrs") Map<String, Attribute> attrs)
	{
		this.id = id;
		this.creatorId = creatorId;
		this.group = group;
		this.attributesByName = attrs == null ? Collections.unmodifiableMap(Collections.emptyMap())
				: Collections.unmodifiableMap(attrs);
	}
	
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