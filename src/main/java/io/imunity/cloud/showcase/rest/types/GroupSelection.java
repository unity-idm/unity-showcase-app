/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */


package io.imunity.cloud.showcase.rest.types;

import java.util.List;

public class GroupSelection
{
	public final List<String> selectedGroups;
	GroupSelection(List<String> selectedGroups)
	{
		this.selectedGroups = selectedGroups;
	}
}