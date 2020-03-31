/**************************************************************************
 *            Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. 
 *                         All rights reserved.
 *                 See LICENCE file for licensing information.
 **************************************************************************/
package io.imunity.cloud.showcase;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MVCController
{
	public enum NavigationItem
	{
		NOTES, USER_PROFILE, BILLINGS, USERS, NEW_NOTEBOOK
	}

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
	public String application(Model model)
	{
		setNavigationItem(model, NavigationItem.NOTES);
		return "application/notes";
	}

	@RequestMapping("/application/notes")
	public String applicationNotes(Model model)
	{
		setNavigationItem(model, NavigationItem.NOTES);
		return "application/notes";
	}

	@RequestMapping("/application/notes/new")
	public String applicationNotesNew(Model model)
	{
		setNavigationItem(model, NavigationItem.NOTES);
		return "application/notes_new";
	}

	@RequestMapping("/application/user")
	public String applicationUser(Model model)
	{
		setNavigationItem(model, NavigationItem.USER_PROFILE);
		return "application/user";
	}
	
	@RequestMapping("/application/billings")
	public String applicationBillings(Model model)
	{
		setNavigationItem(model, NavigationItem.BILLINGS);
		return "application/billings";
	}
	
	@RequestMapping("/application/users")
	public String applicationUsers(Model model)
	{
		setNavigationItem(model, NavigationItem.USERS);
		return "application/users";
	}
	
	@RequestMapping("/application/new")
	public String applicationNew(Model model)
	{
		setNavigationItem(model, NavigationItem.NEW_NOTEBOOK);
		return "application/new_subscription";
	}

	private void setNavigationItem(Model model, NavigationItem selected)
	{
		model.addAttribute("selected_nav", selected);
	}
}
