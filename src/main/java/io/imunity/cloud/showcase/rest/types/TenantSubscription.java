/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantSubscription
{
	private String status;
	private Long startDate;
	private Long endDate;
	private Long cancelDate;
	private Long daysUntilDue;
	private Long currentPeriodStart;
	private Long currentPeriodEnd;
	private String cardLast4;

	@JsonCreator
	public TenantSubscription()
	{

	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Long getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Long startDate)
	{
		this.startDate = startDate;
	}

	public Long getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Long endDate)
	{
		this.endDate = endDate;
	}

	public Long getCancelDate()
	{
		return cancelDate;
	}

	public void setCancelDate(Long cancelDate)
	{
		this.cancelDate = cancelDate;
	}

	public Long getDaysUntilDue()
	{
		return daysUntilDue;
	}

	public void setDaysUntilDue(Long daysUntilDue)
	{
		this.daysUntilDue = daysUntilDue;
	}

	public Long getCurrentPeriodStart()
	{
		return currentPeriodStart;
	}

	public void setCurrentPeriodStart(Long currentPeriodStart)
	{
		this.currentPeriodStart = currentPeriodStart;
	}

	public Long getCurrentPeriodEnd()
	{
		return currentPeriodEnd;
	}

	public void setCurrentPeriodEnd(Long currentPeriodEnd)
	{
		this.currentPeriodEnd = currentPeriodEnd;
	}

	public String getCardLast4()
	{
		return cardLast4;
	}

	public void setCardLast4(String cardLast4)
	{
		this.cardLast4 = cardLast4;
	}

}