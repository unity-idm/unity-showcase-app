/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest.types;

import java.time.Instant;

public class EmailIdentitySelection
{
	public final String typeId = "email";
	public final String value;
	public final ConfirmationInfo confirmationInfo = new ConfirmationInfo();

	EmailIdentitySelection(String value)
	{
		this.value = value;
	}

	public static class ConfirmationInfo
	{
		public final boolean confirmed = true;
		public final long confirmationDate = Instant.now().toEpochMilli();
		public final int sentRequestAmount = 0;
	}
}