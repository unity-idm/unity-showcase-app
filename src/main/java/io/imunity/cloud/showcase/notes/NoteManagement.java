/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.notes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.imunity.cloud.showcase.SecurityConfig;

@Component
public class NoteManagement
{
	private static int LIGHT_NOTES_LIMIT = 5;

	@Autowired
	private NotesRepository notesRepo;

	public List<Note> getNotes(String subsritpionId, String ownerId)
	{

		return StreamSupport.stream(notesRepo.findAll().spliterator(), false).filter(
				n -> n.getOwnerId().equals(ownerId) && n.getSubscriptionId().equals(subsritpionId))
				.collect(Collectors.toList());
	}

	public void addNote(Note note)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean authorized = authorities.contains(new SimpleGrantedAuthority(SecurityConfig.PREMIUM_AUTHORITY));
		if (notesRepo.count() > LIGHT_NOTES_LIMIT && !authorized)
			throw new AccessDeniedException(
					"Only premium users may have more than " + LIGHT_NOTES_LIMIT + " notes");
		notesRepo.save(note);
	}

	public Note getNote(Long id)
	{
		Optional<Note> note = notesRepo.findById(id);

		if (note.isPresent())
		{
			return note.get();
		} else
		{
			throw new IllegalArgumentException("Note with id " + id + " does not exists");
		}
	}

	public void updateNote(Note note)
	{
		notesRepo.save(note);

	}

	public void deleteNote(Long id)
	{
		notesRepo.deleteById(id);
	}
}
