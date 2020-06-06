/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest.types;

public class PrefilledEntry<T>
{
	public final T entry;
	public final PrefilledEntryMode mode;

	public PrefilledEntry(T entry, PrefilledEntryMode mode)
	{
		this.entry = entry;
		this.mode = mode;
	}
}