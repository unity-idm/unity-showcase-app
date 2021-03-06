/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest;

import java.net.URI;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import io.imunity.cloud.showcase.Constans;
import io.imunity.cloud.showcase.rest.types.Attribute;
import io.imunity.cloud.showcase.rest.types.Entity;
import io.imunity.cloud.showcase.rest.types.InvitationParam;
import io.imunity.cloud.showcase.rest.types.Invoice;
import io.imunity.cloud.showcase.rest.types.Tenant;
import io.imunity.cloud.showcase.rest.types.TenantDetails;
import io.imunity.cloud.showcase.rest.types.TenantSubscription;
import io.imunity.cloud.showcase.rest.types.TenantUser;

@Component
public class UnityRestClient
{
	@Value("${unity.baseuri}")
	private String unityBaseUrl;

	@Value("${unity.tenantEndpoint}")
	private String tenantEndpoint;

	@Value("${unity.rest.client.username}")
	private String unityRestClientUsername;

	@Value("${unity.rest.client.password}")
	private String unityRestClientPassword;

	@Value("${unity.rest.uri}")
	private String unityRestUrl;

	@Value("${unity.tenantUserRegistrationForm}")
	private String unityTenantUserRegistrationForm;

	@Value("${unity.oauth2.token-revoke-uri}")
	private String tokenRevokeUri;

	@Value("${spring.security.oauth2.client.registration.unity.clientId}")
	private String oauthClient;

	public Optional<TenantUser> getUser(String subscriptionId, String userId)
	{
		List<TenantUser> users = getUsers(subscriptionId);
		return users.stream().filter(u -> u.getId().equals(userId)).findAny();
	}

	public List<TenantUser> getUsers(String subscriptionId)
	{
		List<TenantUser> users = null;
		try
		{
			users = Constans.MAPPER.readValue(
					requestToUnityRESTCloudService("users/" + subscriptionId, HttpMethod.GET),
					new TypeReference<List<TenantUser>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get users", e);
		}
		return users;
	}

	public void deleteUser(String subscriptionId, String userId) throws Exception
	{
		requestToUnityRESTCloudService("user/" + subscriptionId + "/" + userId, HttpMethod.DELETE);
	}

	public TenantDetails getSubscriptionDetails(String id)
	{
		try
		{
			Tenant tenant = Constans.MAPPER.readValue(
					requestToUnityRESTCloudService("tenant/" + id, HttpMethod.GET), Tenant.class);

			TenantSubscription subscription = getSubscription(id);
			TenantDetails sb = new TenantDetails(tenant, subscription);
			return sb;

		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscription", e);
		}
	}

	private TenantSubscription getSubscription(String id)
	{
		try
		{
			return Constans.MAPPER.readValue(
					requestToUnityRESTCloudService("subscription/" + id, HttpMethod.GET),
					TenantSubscription.class);

		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscription details", e);
		}
	}

	public Map<String, String> getSubscriptions(String userId)
	{

		List<Tenant> tenants = null;
		try
		{
			tenants = Constans.MAPPER.readValue(
					requestToUnityRESTCloudService("tenants/" + userId, HttpMethod.GET),
					new TypeReference<List<Tenant>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscriptions", e);
		}

		return tenants.stream().collect(Collectors.toMap(t -> t.id,
				t -> t.attributesByName.get(Constans.TENANT_NAME_ATTR).getValues().get(0)));
	}

	public List<Invoice> getInvoices(String subscriptionId)
	{

		List<Invoice> invoices = null;
		try
		{
			invoices = Constans.MAPPER.readValue(
					requestToUnityRESTCloudService("invoices/" + subscriptionId, HttpMethod.GET),
					new TypeReference<List<Invoice>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get invoices", e);
		}

		return invoices;
	}

	public boolean addToGroupOrInvite(String email, String subscriptionGroup)
	{
		Entity entity = null;
		try
		{
			entity = Constans.MAPPER
					.readValue(requestToUnityRESTService("entity/" + email + "?identityType=email",
							HttpMethod.GET, Optional.empty()), Entity.class);

		} catch (Exception e)
		{
			// ok
		}

		if (entity == null)
		{
			try
			{
				InvitationParam param = new InvitationParam(unityTenantUserRegistrationForm,
						Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli(), email,
						subscriptionGroup + "/users");
				String code = requestToUnityRESTService("invitation/", HttpMethod.POST,
						Optional.of(Constans.MAPPER.writeValueAsString(param)));
				requestToUnityRESTService("invitation/" + code + "/send", HttpMethod.POST,
						Optional.empty());
				return true;
			} catch (Exception e)
			{
				throw new RuntimeException("Can not add invitation", e);
			}
		} else
		{
			try
			{
				requestToUnityRESTService(
						"group/" + URLEncoder.encode(subscriptionGroup + "/users", "UTF-8")
								+ "/entity/"
								+ entity.getEntityInformation().getEntityId(),
						HttpMethod.POST, Optional.empty());
			} catch (Exception e)
			{
				throw new RuntimeException("Can not add user to group", e);
			}
		}
		return false;

	}

	public void cancelSubscription(String id)
	{
		try
		{
			requestToUnityRESTCloudService("subscription/" + id, HttpMethod.DELETE);
		} catch (Exception e)
		{
			throw new RuntimeException("Can not cancel subscription", e);
		}
	}

	public void renewSubscription(String id)
	{
		try
		{
			requestToUnityRESTCloudService("subscription/renew/" + id, HttpMethod.GET);
		} catch (Exception e)
		{
			throw new RuntimeException("Can not renew subscription", e);
		}
	}

	public void updateStringRootAttribute(String entityId, String name, String value) throws Exception
	{
		Attribute a = new Attribute();
		a.setName(name);
		a.setValues(Arrays.asList(value));
		a.setValueSyntax("string");
		a.setGroupPath("/");

		try
		{
			requestToUnityRESTService("entity/" + entityId + "/attribute?identityType=persistent",
					HttpMethod.PUT, Optional.of(Constans.MAPPER.writeValueAsString(a)));
		} catch (JsonProcessingException e)
		{
			throw new RuntimeException("Can not update attribute", e);
		}
	}

	public void revokeToken(String token)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);
		map.add("token_type_hint", "access_token");
		map.add("client_id", oauthClient);
		map.add("logout", "true");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(tokenRevokeUri, request, String.class);
		if (!(HttpStatus.OK.equals(response.getStatusCode())))
		{
			throw new RuntimeException("Communication with unity service error");
		}
	}

	private RestTemplate getRestTemplateWithBasicAuthn()
	{
		return new RestTemplateBuilder().basicAuthentication(unityRestClientUsername, unityRestClientPassword)
				.build();
	}

	private String requestToUnityRESTCloudService(String restUrl, HttpMethod method) throws Exception
	{
		return requestToUnityRESTService(unityBaseUrl + "/" + tenantEndpoint + Constans.API_POSTFIX + "/",
				restUrl, method, Optional.empty());
	}

	private String requestToUnityRESTService(String restUrl, HttpMethod method, Optional<String> entity) throws Exception
	{
		return requestToUnityRESTService(unityRestUrl, restUrl, method, entity);
	}

	private String requestToUnityRESTService(String mainRestUrl, String restUrl, HttpMethod method,
			Optional<String> entity) throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		URI uri = URI.create(mainRestUrl + restUrl);
		
		ResponseEntity<String> exchange;
		try
		{
			exchange = getRestTemplateWithBasicAuthn().exchange(uri, method,
					entity.isPresent() ? new HttpEntity<String>(entity.get(), headers) : null,
					String.class);
		} catch (HttpClientErrorException ex)
		{
			throw Constans.MAPPER.readValue(ex.getResponseBodyAsString(), UnityException.class);
		}

		if (!(HttpStatus.OK.equals(exchange.getStatusCode())
				|| HttpStatus.NO_CONTENT.equals(exchange.getStatusCode())))
		{
			throw new RuntimeException("Communication with unity service error");
		}

		return exchange.getBody();
	}
	
	public static class UnityException extends Exception
	{
		public final String error;
		
		@JsonCreator
		public UnityException(@JsonProperty("message") String message,@JsonProperty("error") String error)
		{
			super(message);
			this.error = error;
		}
	}
}
