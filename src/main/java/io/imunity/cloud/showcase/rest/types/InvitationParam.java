/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InvitationParam
{
	

	public final String type = "REGISTRATION";
	public final String formId;
	public final long expiration;
	public final String contactAddress;
	public final Map<Integer, PrefilledEntry<EmailIdentitySelection>> identities;
	public final Map<Integer, String> attributes = new HashMap<>();
	public final Map<Integer, PrefilledEntry<GroupSelection>> groupSelections;

	public InvitationParam(String formId, long expiration, String contactAddress, String group)
	{
		this.formId = formId;
		this.expiration = expiration;
		this.contactAddress = contactAddress;
		this.groupSelections = new HashMap<>();
		this.groupSelections.put(0, new PrefilledEntry<GroupSelection>(new GroupSelection(Arrays.asList(group)),
				PrefilledEntryMode.HIDDEN));
		this.identities = new HashMap<>();
		this.identities.put(0,
				new PrefilledEntry<EmailIdentitySelection>(
						new EmailIdentitySelection(contactAddress),
						PrefilledEntryMode.READ_ONLY));
	}
}