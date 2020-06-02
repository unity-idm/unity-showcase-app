/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.notes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Note
{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String subscriptionId;
	private String ownerId;
	private String title;
	private String content;
	
	public Note()
	{
		
	}

	public Note(Long id, String subscriptionId, String ownerId, String title, String content)
	{
		
		this.id = id;
		this.setSubscriptionId(subscriptionId);
		this.setOwnerId(ownerId);
		this.title = title;
		this.content = content;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}

	public String getSubscriptionId()
	{
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId)
	{
		this.subscriptionId = subscriptionId;
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
