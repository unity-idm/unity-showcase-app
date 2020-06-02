/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.notes;

import org.springframework.data.repository.CrudRepository;

public interface NotesRepository extends CrudRepository<Note, Long>
{
}
