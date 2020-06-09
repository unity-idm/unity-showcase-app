/**************************************************************************
 *            Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. 
 *                         All rights reserved.
 *                 See LICENCE file for licensing information.
 **************************************************************************/
package io.imunity.cloud.showcase;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.imunity.cloud.showcase.notes.Note;
import io.imunity.cloud.showcase.notes.NoteManagement;
import io.imunity.cloud.showcase.rest.UnityRestClient;
import io.imunity.cloud.showcase.rest.types.TenantUser;
import io.imunity.cloud.showcase.subscription.SubscriptionRepository;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SessionAttributes({ "context" })
public class MVCController
{
	private static final String REQUEST_URL = "URL_PRIOR_LOGIN";
	private static final String SPRING_SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

	@Value("${unity.tenantAdminRegistrationUrl}")
	private String unityTenantAdminRegistrationUrl;

	@Value("${unity.baseurl}")
	private String unityBaseUrl;

	@Value("${unity.tenantEndpoint}")
	private String tenantEndpoint;

	@Autowired
	private UnityRestClient unityRestClient;

	@Autowired
	private NoteManagement noteMan;

	@Autowired
	private SubscriptionRepository subscriptionMan;

	@Autowired
	private InvocationContext context;

	@Autowired
	private InvocationContextUpdater secContextUpdater;

	@RequestMapping("/")
	public String main(Model model)
	{
		if (context.getSubscription() == null)
		{
			model.addAttribute("registrationUrl", unityTenantAdminRegistrationUrl);
			return "pricing";
		} else
		{
			return redirect("application/notes");
		}
	}

	@GetMapping(value = "/select-subscription")
	public String selectSubscription(Authentication authentication, Model model, HttpServletRequest request,
			@RequestParam(value = "change") Optional<Boolean> change)
	{
		if ((change.isEmpty() || !change.get()) && context.getSubscription() != null)
		{
			return redirect("application/notes");
		}

		Map<String, String> filteredSubscriptions = getSubscriptions(authentication);
		
		if (filteredSubscriptions.size() == 0)
		{
			return "redirect:" + unityBaseUrl + "/" + tenantEndpoint + Constans.SIGNUP_PATH;
		}

		if (filteredSubscriptions.size() == 1)
		{
			return selectSubscription(authentication, filteredSubscriptions.keySet().iterator().next(), request);
		}
		model.addAttribute("subs", filteredSubscriptions);
		model.addAttribute("subscription", filteredSubscriptions.keySet().iterator().next());

		return "select_subscription";
	}
	
	private Map<String, String> getSubscriptions(Authentication authentication)
	{
		Map<String, String> remoteSubscriptions = unityRestClient
				.getSubscriptions(InvocationContextUpdater.getUserId(authentication));

		return StreamSupport
				.stream(subscriptionMan.findAll().spliterator(), false)
				.filter(s -> remoteSubscriptions.keySet().contains(s.getId()))
				.collect(Collectors.toMap(sub -> sub.getId(), sub -> sub.getName()));
	}

	@PostMapping("/selected-subscription")
	public String selectSubscription(Authentication authentication, String subscription, HttpServletRequest request)
	{
		secContextUpdater.updateSecurityContext(authentication, subscription);
		String urlPriorLogin = (String) request.getSession().getAttribute(REQUEST_URL);
		return redirect((urlPriorLogin != null ? urlPriorLogin : "/application/notes"));
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application")
	public String application()
	{
		return redirect("application/notes");
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/notes")
	public String applicationNotes(Model model)
	{
		model.addAttribute("notes", noteMan.getNotes(context.getSubscriptionId(), context.getUserId()));
		return "application/notes";
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/notes/new")
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
		note.setSubscriptionId(context.getSubscription().tenant.id);
		try
		{
			noteMan.addNote(note);
			return redirect("/application/notes");
		} catch (Exception ex)
		{
			model.addAttribute("add", true);
			model.addAttribute("errorMessage", ex.getMessage());
			return "application/notes_edit";
		}
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/note/{noteId}/edit")
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
			note.setSubscriptionId(context.getSubscriptionId());
			noteMan.updateNote(note);
		} catch (Exception ex)
		{
			redir.addFlashAttribute("errorMessage", ex.getMessage());
		}

		RedirectView redirectView = new RedirectView("/application/notes", true);
		return redirectView;
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/note/{noteId}/delete")
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
	@GetMapping("/application/user/{userId}/delete")
	public String applicationUser(Model model, @PathVariable String userId)
	{
		unityRestClient.deleteUser(context.getSubscription().tenant.id, userId);
		return redirect("/application/users");
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/application/billings")
	public String applicationBillings(Authentication authentication, Model model,
			@RequestParam("updateStatus") Optional<String> update,
			@RequestParam("updatePaymentMethodStatus") Optional<String> updatePaymentMethod)
	{

		String id = context.getSubscriptionId();

		if (!updatePaymentMethod.isEmpty() || !update.isEmpty())
		{
			secContextUpdater.updateSecurityContext(authentication, id);
		}

		model.addAttribute("updateUrl", unityBaseUrl + "/" + tenantEndpoint
				+ Constans.PAYMENT_METHOD_UPDATE_PATH + "?" + Constans.TENANT_ID_PARAM + "=" + id);
		model.addAttribute("upgradeUrl", unityBaseUrl + "/" + tenantEndpoint + Constans.SUBSCRIPTION_UPDATE_PATH
				+ "?" + Constans.TENANT_ID_PARAM + "=" + id);
		model.addAttribute("owner",
				unityRestClient.getUser(id, context.getSubscription().tenant.creatorId).get());
		model.addAttribute("invoices", unityRestClient.getInvoices(id));

		return "application/billings";
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/subscription/cancel")
	public String cancelSubscription(Authentication authentication)
	{
		unityRestClient.cancelSubscription(context.getSubscriptionId());
		secContextUpdater.updateSecurityContext(authentication, context.getSubscriptionId());
		return redirect("/application/billings");
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/subscription/renew")
	public String renewSubscription(Authentication authentication)
	{
		unityRestClient.renewSubscription(context.getSubscription().tenant.id);
		secContextUpdater.updateSecurityContext(authentication, context.getSubscriptionId());
		return redirect("/application/billings");
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/users")
	public String applicationUsers(Model model)
	{
		model.addAttribute("users", unityRestClient.getUsers(context.getSubscriptionId()));
		return "application/users";
	}

	@PreAuthorize("hasAuthority('ADMIN') && hasAuthority('ACTIVE_ACCOUNT')")
	@PostMapping("/application/user/invite")
	public RedirectView applicationUserInvite(String email, RedirectAttributes redir)
	{
		unityRestClient.addInvitation(email, context.getSubscription().tenant.group);
		RedirectView redirectView = new RedirectView("/application/users", true);
		redir.addFlashAttribute("invitationEmail", email);
		return redirectView;
	}

	@PreAuthorize("hasAuthority('ACTIVE_ACCOUNT')")
	@GetMapping("/application/user")
	public String applicationUser(Model model)
	{
		TenantUser usr = unityRestClient.getUser(context.getSubscriptionId(), context.getUserId()).orElse(null);
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
		unityRestClient.updateStringRootAttribute(context.getUserId(), Constans.FIRSTNAME_ATTR, firstname);
		unityRestClient.updateStringRootAttribute(context.getUserId(), Constans.SURNAME_ATTR, surname);
		redir.addFlashAttribute("updated", true);
		RedirectView redirectView = new RedirectView("/application/user", true);
		return redirectView;
	}

	@GetMapping("/application/new")
	public String applicationNew(Model model)
	{
		model.addAttribute("registrationUrl", unityBaseUrl + "/" + tenantEndpoint + Constans.SIGNUP_PATH);
		return "application/new_subscription";
	}

	@GetMapping("/login")
	public String login(Principal principal, HttpServletRequest request, HttpSession session)
	{
		if (principal != null)
			return redirect("/application/notes");
		DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute(SPRING_SAVED_REQUEST);
		if (savedRequest != null)
			request.getSession().setAttribute(REQUEST_URL, savedRequest.getRequestURI());
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
		{
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "login";
	}

	@GetMapping("/access_denied")
	public String handleError(Model model)
	{
		if (context.getSubscription() == null)
		{
			return redirect("/select-subscription");
		}

		return "access_denied";
	}

	@ModelAttribute("context")
	public InvocationContext populateInvocationContext()
	{
		return context;
	}

	private String redirect(String to)
	{
		return "redirect:" + to;
	}
}
