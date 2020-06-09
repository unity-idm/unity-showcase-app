/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Constans
{
	
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static final String NAME_ATTR = "name";
	public static final String FIRSTNAME_ATTR = "firstname";
	public static final String SURNAME_ATTR = "surname";
	public static final String EMAIL_ATTR = "email";

	public static final String SUBSCRIPTION_APP_ROLE = "sys:approle";
	public static final String TENANT_PLAN_ATTR = "sys:client-selected-plan";
	public static final String TENANT_NAME_ATTR = "company";

	public static final String ACTIVE_SUBSCRIPTION = "ACTIVE";
	
	public static final String PAYMENT_METHOD_UPDATE_PATH = "/paymentMethodUpdate";
	public static final String SUBSCRIPTION_UPDATE_PATH = "/subscriptionUpdate";
	public static final String SIGNUP_PATH = "/signup";
	
	public static final String API_POSTFIX = "-api";
	
	public static final String TENANT_ID_PARAM = "tenantId";
}
