/**************************************************************************
 *            Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. 
 *                         All rights reserved.
 *                 See LICENCE file for licensing information.
 **************************************************************************/
package io.imunity.cloud.showcase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MVCController
{
	@RequestMapping("/")
	public String main()
	{
		return "pricing";
	}

	@RequestMapping("/select-subscription")
	public String selectTenant()
	{
		return "select_subscription";
	}

	@RequestMapping("/application")
	public String application()
	{
		return "application/notes";
	}

	@RequestMapping("/application/notes")
	public String applicationNotes()
	{
		return "application/notes";
	}

	@RequestMapping("/application/notes/new")
	public String applicationNotesNew()
	{
		return "application/notes_new";
	}

	@RequestMapping("/application/user")
	public String applicationUser()
	{
		return "application/user";
	}
	
	@RequestMapping("/application/billings")
	public String applicationBillings()
	{
		return "application/billings";
	}
	
	@RequestMapping("/application/users")
	public String applicationUsers()
	{
		return "application/users";
	}
	
	@RequestMapping("/application/new")
	public String applicationNew()
	{
		return "application/new_subscription";
	}
}
