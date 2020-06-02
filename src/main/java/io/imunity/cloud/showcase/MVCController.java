/**************************************************************************
 *            Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. 
 *                         All rights reserved.
 *                 See LICENCE file for licensing information.
 **************************************************************************/
package io.imunity.cloud.showcase;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.imunity.cloud.showcase.notes.Note;
import io.imunity.cloud.showcase.notes.NoteManagement;
import io.imunity.cloud.showcase.rest.UnityRestClient;
import io.imunity.cloud.showcase.rest.UnityRestClient.SubscriptionUser;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MVCController
{
	private static final String REQUEST_URL = "URL_PRIOR_LOGIN";
	private static final String SPRING_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

	@Value("${unity.tenantAdminRegistrationForm}")
	private String unityTenantAdminRegistrationForm;

	@Value("${unity.updatePaymentMethodUrl}")
	private String unityUpdatePaymentMethodUrl;

	@Value("${unity.updateSubscriptionUrl}")
	private String unityUpdateSubscriptionUrl;

	@Value("${unity.newSubscriptionUrl}")
	private String unityNewSubscriptionUrl;

	@Autowired
	private UnityRestClient unityRestClient;

	@Autowired
	private NoteManagement noteMan;

	@Autowired
	private InvocationContext context;

	@Autowired
	private SecurityContextUpdater secContextUpdater;

	@RequestMapping("/")
	public String main(Model model)
	{
		model.addAttribute("registrationUrl", unityTenantAdminRegistrationForm);
		return "pricing";
	}

	@RequestMapping(value = "/select-subscription", method = RequestMethod.GET)
	public String selectSubscription(Authentication authentication, Model model, HttpServletRequest request)
	{
		if (context.getSubscritpion() != null)
		{
			return "redirect:application/notes";
		}

		Map<String, String> subscriptions = unityRestClient
				.getSubscriptions(SecurityContextUpdater.getUserId(authentication));

		if (subscriptions.size() == 0)
		{
			return "redirect:" + unityTenantAdminRegistrationForm;
		}

		if (subscriptions.size() == 1)
		{
			return selectSubscription(authentication, subscriptions.keySet().iterator().next(), request);
		}
		model.addAttribute("subs", subscriptions);
		model.addAttribute("subscription", subscriptions.keySet().iterator().next());

		return "select_subscription";
	}

	@PostMapping("/select-subscription")
	public String selectSubscription(Authentication authentication, String subscription, HttpServletRequest request)
	{
		secContextUpdater.updateSecurityContext(authentication, subscription);
		String urlPriorLogin = (String) request.getSession().getAttribute(REQUEST_URL);
		return "redirect:" + (urlPriorLogin != null ? urlPriorLogin : "/application/notes");
	}

	@RequestMapping("/application")
	public String application()
	{
		return "redirect:application/notes";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping("/application/notes")
	public String applicationNotes(Model model)
	{
		model.addAttribute("notes", noteMan.getNotes(context.getSubscritpion().getId(), context.getUserId()));
		return "application/notes";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping("/application/notes/new")
	public String applicationNotesNew(Authentication authentication, Model model)
	{
		Note note = new Note();
		model.addAttribute("note", note);
		model.addAttribute("add", true);
		return "application/notes_edit";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping(value = "/application/notes/add")
	public String addNote(Model model, @ModelAttribute("note") Note note)
	{
		note.setOwnerId(context.getUserId());
		note.setSubscriptionId(context.getSubscritpion().getId());
		try
		{
			noteMan.addNote(note);
			return "redirect:/application/notes";
		} catch (Exception ex)
		{
			model.addAttribute("add", true);
			model.addAttribute("errorMessage", ex.getMessage());
			return "application/notes_edit";
		}
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping(value = { "/application/note/{noteId}/edit" })
	public String updateNote(Model model, @PathVariable long noteId)
	{
		Note note = null;
		model.addAttribute("add", false);
		try
		{
			note = noteMan.getNote(noteId);
		} catch (IllegalArgumentException ex)
		{
			model.addAttribute("errorMessage", ex.getMessage());
			return "application/notes_edit";

		}

		model.addAttribute("note", note);
		return "application/notes_edit";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping(value = { "/application/note/{noteId}/edit" })
	public RedirectView updateNote(Model model, @PathVariable long noteId, @ModelAttribute("note") Note note,
			RedirectAttributes redir)
	{
		note.setId(noteId);
		note.setOwnerId(context.getUserId());
		try
		{
			note.setSubscriptionId(context.getSubscritpion().getId());
			noteMan.updateNote(note);
		} catch (Exception ex)
		{
			redir.addFlashAttribute("errorMessage", ex.getMessage());
		}

		RedirectView redirectView = new RedirectView("/application/notes", true);
		return redirectView;
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping(value = { "/application/note/{noteId}/delete" })
	public RedirectView deleteNote(Model model, @PathVariable long noteId, RedirectAttributes redir)
	{
		try
		{
			noteMan.deleteNote(noteId);
		} catch (Exception ex)
		{
			redir.addFlashAttribute("errorMessage", ex.getMessage());
		}
		RedirectView redirectView = new RedirectView("/application/notes", true);
		return redirectView;
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping(value = { "/application/user/{userId}/delete" })
	public String applicationUser(Model model, @PathVariable String userId)
	{
		unityRestClient.deleteUser(context.getSubscritpion().getId(), userId);
		return "redirect:/application/users";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/application/billings")
	public String applicationBillings(Authentication authentication, Model model,
			@RequestParam("updateStatus") Optional<String> update,
			@RequestParam("updatePaymentMethodStatus") Optional<String> updatePaymentMethod)
	{
		if (!updatePaymentMethod.isEmpty() || !update.isEmpty())
		{
			secContextUpdater.updateSecurityContext(authentication, context.getSubscritpion().getId());
		}

		model.addAttribute("updateUrl",
				unityUpdatePaymentMethodUrl + "?tenantId=" + context.getSubscritpion().getId());
		model.addAttribute("upgradeUrl",
				unityUpdateSubscriptionUrl + "?tenantId=" + context.getSubscritpion().getId());
		model.addAttribute("subscription", context.getSubscritpion());
		model.addAttribute("owner", unityRestClient
				.getUser(context.getSubscritpion().getId(), context.getSubscritpion().getCreatorId())
				.get());
		model.addAttribute("invoices", unityRestClient.getInvoices(context.getSubscritpion().getId()));

		return "application/billings";
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/subscription/cancel")
	public String cancelSubscription(HttpServletRequest request, HttpServletResponse response)
	{
		unityRestClient.cancelSubscription(context.getSubscritpion().getId());
		return logout(request, response);
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping("/application/users")
	public String applicationUsers(Model model)
	{
		model.addAttribute("users", unityRestClient.getUsers(context.getSubscritpion().getId()));
		return "application/users";
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/user/invite")
	public RedirectView applicationUserInvite(String email, RedirectAttributes redir)
	{
		unityRestClient.addInvitation(email);
		RedirectView redirectView = new RedirectView("/application/users", true);
		redir.addFlashAttribute("invitationEmail", email);
		return redirectView;
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@RequestMapping("/application/user")
	public String applicationUser(Model model)
	{
		SubscriptionUser usr = unityRestClient.getUser(context.getSubscritpion().getId(), context.getUserId())
				.orElse(null);
		if (usr != null)
		{
			model.addAttribute("firstname", usr.getFirstname());
			model.addAttribute("surname", usr.getSurname());
			model.addAttribute("email", usr.getEmail());
		}
		return "application/user";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/user")
	public RedirectView applicationUserUpdate(String firstname, String surname, RedirectAttributes redir)
	{
		unityRestClient.updateStringRootAttribute(context.getUserId(), UnityRestClient.FIRSTNAME_ATTR,
				firstname);
		unityRestClient.updateStringRootAttribute(context.getUserId(), UnityRestClient.SURNAME_ATTR, surname);
		redir.addFlashAttribute("updated", true);
		RedirectView redirectView = new RedirectView("/application/user", true);
		return redirectView;
	}

	@RequestMapping("/application/new")
	public String applicationNew(Model model)
	{
		model.addAttribute("registrationUrl", unityNewSubscriptionUrl);
		return "application/new_subscription";
	}

	@RequestMapping("/login")
	public String login(Principal principal, HttpServletRequest request, HttpSession session)
	{
		if (principal != null)
			return "redirect:/application/notes";
		DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute(SPRING_SAVED_REQUEST);
		if (savedRequest != null)
			request.getSession().setAttribute(REQUEST_URL, savedRequest.getRequestURI());
		return "login";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
		{
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "login";
	}

	@RequestMapping("/access_denied")
	public String handleError(Model model)
	{
		return "access_denied";
	}
}
