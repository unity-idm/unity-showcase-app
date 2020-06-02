/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase.rest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




@Component
public class UnityRestClient
{
	public static final String SUBSCRIPTION_APP_ROLE = "sys:approle";
	public static final String TENANT_PLAN_ATTR = "sys:client-selected-plan";
	public static final String TENANT_NAME_ATTR = "company";

	public static final String NAME_ATTR = "name";
	public static final String FIRSTNAME_ATTR = "firstname";
	public static final String SURNAME_ATTR = "surname";
	public static final String EMAIL_ATTR = "email";

	public static final ObjectMapper MAPPER = new ObjectMapper();

	@Value("${unity.rest.client.username}")
	private String unityRestClientUsername;
	@Value("${unity.rest.client.password}")
	private String unityRestClientPassword;
	@Value("${unity.rest.url}")
	private String unityRestUrl;
	@Value("${unity.rest.cloud.url}")
	private String unityRestCloudUrl;
	@Value("${unity.tenantUserRegistrationForm}")
	private String unityTenantUserRegistrationForm;


	public Optional<SubscriptionUser> getUser(String subscriptionId, String userId)
	{
		List<SubscriptionUser> users = getUsers(subscriptionId);
		return users.stream().filter(u -> u.id.equals(userId)).findAny();
	}

	public List<SubscriptionUser> getUsers(String subscriptionId)
	{
		List<SubscriptionUser> users = null;
		try
		{
			users = MAPPER.readValue(
					requestToUnityRESTCloudService("users/" + subscriptionId, HttpMethod.GET),
					new TypeReference<List<SubscriptionUser>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get users", e);
		}
		return users;
	}

	public void deleteUser(String subscriptionId, String userId)
	{
		requestToUnityRESTCloudService("user/" + subscriptionId + "/" + userId, HttpMethod.DELETE);
	}

	public SubscriptionDetails getSubscriptionDetails(String id)
	{
		try
		{
			SubscriptionBase tenant = MAPPER.readValue(requestToUnityRESTCloudService("tenant/" + id, HttpMethod.GET),
					SubscriptionBase.class);
			Attribute role = tenant.attributesByName.get(TENANT_PLAN_ATTR);
			boolean premium = role != null && role.values.size() > 0
					&& role.values.get(0).toLowerCase().equals("premium");
			Subscription subscription = getSubscription(id);
			SubscriptionDetails sb = new SubscriptionDetails();
			sb.setId(id);
			sb.setName(tenant.attributesByName.get(TENANT_NAME_ATTR).values.get(0));
			sb.setCreatorId(tenant.creatorId);
			sb.setCardLast4(subscription.getCardLast4());
			sb.setCancelDate(subscription.getCancelDate());
			sb.setEndDate(subscription.getEndDate());
			sb.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
			sb.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
			sb.setDaysUntilDue(subscription.getDaysUntilDue());
			sb.setPremium(premium);
			sb.setStartDate(subscription.getStartDate());
			sb.setStatus(subscription.getStatus());
			return sb;
			
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscription", e);
		}
	}
	
	private Subscription getSubscription(String id)
	{
		try
		{
			return MAPPER.readValue(requestToUnityRESTCloudService("subscription/" + id, HttpMethod.GET),
					Subscription.class);
			
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscription details", e);
		}
	}

	public Map<String, String> getSubscriptions(String userId)
	{

		List<SubscriptionBase> tenants = null;
		try
		{
			tenants = MAPPER.readValue(requestToUnityRESTCloudService("tenants/" + userId, HttpMethod.GET),
					new TypeReference<List<SubscriptionBase>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get subscriptions", e);
		}

		return tenants.stream().collect(Collectors.toMap(t -> t.id,
				t -> t.attributesByName.get(TENANT_NAME_ATTR).values.get(0)));
	}
	
	public List<Invoice> getInvoices(String subscriptionId)
	{

		List<Invoice> invoices = null;
		try
		{
			invoices = MAPPER.readValue(requestToUnityRESTCloudService("invoices/" + subscriptionId, HttpMethod.GET),
					new TypeReference<List<Invoice>>()
					{
					});
		} catch (Exception e)
		{
			throw new RuntimeException("Can not get invoices", e);
		}

		return invoices;
	}
	
	public void addInvitation(String email)
	{
		try
		{
			String code = requestToUnityRESTService("invitation/", HttpMethod.POST,
					Optional.of(MAPPER.writeValueAsString(new InvitationParam(
							unityTenantUserRegistrationForm,
							Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli(),
							email))));
			requestToUnityRESTService("invitation/" + code + "/send", HttpMethod.POST, Optional.empty());
		} catch (Exception e)
		{
			throw new RuntimeException("Can not add invitation", e);
		}

	}
	
	public void cancelSubscription(String id)
	{
		try
		{
			requestToUnityRESTCloudService("subscription/" + id, HttpMethod.DELETE);
		} catch (Exception e)
		{
			throw new RuntimeException("Can not add invitation", e);
		}
	}
	
	public void updateStringRootAttribute(String entityId, String name, String value)
	{
		Attribute a = new Attribute();
		a.setName(name);
		a.setValues(Arrays.asList(value));
		a.setValueSyntax("string");
		a.setGroupPath("/");
		
		try
		{
			requestToUnityRESTService("entity/" + entityId + "/attribute?identityType=persistent", HttpMethod.PUT,
					Optional.of(MAPPER.writeValueAsString(a)));
		} catch (JsonProcessingException e)
		{
			throw new RuntimeException("Can not update attribute", e);
		}
	}

	private RestTemplate getRestTemplate()
	{
		return new RestTemplateBuilder().basicAuthentication(unityRestClientUsername, unityRestClientPassword)
				.build();
	}

	private String requestToUnityRESTCloudService(String restUrl, HttpMethod method)
	{
		return requestToUnityRESTService(unityRestCloudUrl, restUrl, method, Optional.empty());
	}

	private String requestToUnityRESTService(String restUrl, HttpMethod method, Optional<String> entity)
	{
		return requestToUnityRESTService(unityRestUrl, restUrl, method, entity);
	}

	private String requestToUnityRESTService(String mainRestUrl, String restUrl, HttpMethod method, Optional<String> entity)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<String> exchange = getRestTemplate().exchange(mainRestUrl + restUrl, method,
				entity.isPresent() ? new HttpEntity<String>(entity.get(), headers) : null,
				String.class);

		if (!(HttpStatus.OK.equals(exchange.getStatusCode())
				|| HttpStatus.NO_CONTENT.equals(exchange.getStatusCode())))
		{
			throw new RuntimeException("Communication with unity service error");
		}

		return exchange.getBody();
	}

	public static class SubscriptionDetails
	{
		private String id;
		private String creatorId;
		private String name;
		private boolean premium;
		private  String status;
		private  Long startDate;
		private  Long endDate;
		private  Long cancelDate;
		private  Long daysUntilDue;
		private  Long currentPeriodStart;
		private  Long currentPeriodEnd;
		private  String cardLast4;

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getCreatorId()
		{
			return creatorId;
		}

		public void setCreatorId(String creatorId)
		{
			this.creatorId = creatorId;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public boolean isPremium()
		{
			return premium;
		}

		public void setPremium(Boolean premium)
		{
			this.premium = premium;
		}

		public String getStatus()
		{
			return status;
		}

		public void setStatus(String status)
		{
			this.status = status;
		}

		public Long getStartDate()
		{
			return startDate;
		}

		public void setStartDate(Long startDate)
		{
			this.startDate = startDate;
		}

		public Long getEndDate()
		{
			return endDate;
		}

		public void setEndDate(Long endDate)
		{
			this.endDate = endDate;
		}

		public Long getCancelDate()
		{
			return cancelDate;
		}

		public void setCancelDate(Long cancelDate)
		{
			this.cancelDate = cancelDate;
		}

		public Long getDaysUntilDue()
		{
			return daysUntilDue;
		}

		public void setDaysUntilDue(Long daysUntilDue)
		{
			this.daysUntilDue = daysUntilDue;
		}

		public Long getCurrentPeriodStart()
		{
			return currentPeriodStart;
		}

		public void setCurrentPeriodStart(Long currentPeriodStart)
		{
			this.currentPeriodStart = currentPeriodStart;
		}

		public Long getCurrentPeriodEnd()
		{
			return currentPeriodEnd;
		}

		public void setCurrentPeriodEnd(Long currentPeriodEnd)
		{
			this.currentPeriodEnd = currentPeriodEnd;
		}

		public String getCardLast4()
		{
			return cardLast4;
		}

		public void setCardLast4(String cardLast4)
		{
			this.cardLast4 = cardLast4;
		}

		public SubscriptionDetails()
		{
			
		}
		
		
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Invoice
	{
		private String number;
		private  Long created;
		private  String currency;
		private  Long total;
		private String status;
		private  String url;
		
		@JsonCreator
		public Invoice()
		{
			
		}
		
		public String getNumber()
		{
			return number;
		}

		public void setNumber(String number)
		{
			this.number = number;
		}

		public Long getCreated()
		{
			return created;
		}

		public void setCreated(Long created)
		{
			this.created = created;
		}

		public String getCurrency()
		{
			return currency;
		}

		public void setCurrency(String currency)
		{
			this.currency = currency;
		}

		public Long getTotal()
		{
			return total;
		}

		public void setTotal(Long total)
		{
			this.total = total;
		}

		public String getUrl()
		{
			return url;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}

		public String getStatus()
		{
			return status;
		}

		public void setStatus(String status)
		{
			this.status = status;
		}
	}
	
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Subscription
	{
		private  String status;
		private  Long startDate;
		private  Long endDate;
		private  Long cancelDate;
		private  Long daysUntilDue;
		private  Long currentPeriodStart;
		private  Long currentPeriodEnd;
		private  String cardLast4;
		
		@JsonCreator
		public Subscription()
		{
			
			
		}
				
		public String getStatus()
		{
			return status;
		}

		public void setStatus(String status)
		{
			this.status = status;
		}

		public Long getStartDate()
		{
			return startDate;
		}

		public void setStartDate(Long startDate)
		{
			this.startDate = startDate;
		}

		public Long getEndDate()
		{
			return endDate;
		}

		public void setEndDate(Long endDate)
		{
			this.endDate = endDate;
		}

		public Long getCancelDate()
		{
			return cancelDate;
		}

		public void setCancelDate(Long cancelDate)
		{
			this.cancelDate = cancelDate;
		}

		public Long getDaysUntilDue()
		{
			return daysUntilDue;
		}

		public void setDaysUntilDue(Long daysUntilDue)
		{
			this.daysUntilDue = daysUntilDue;
		}

		public Long getCurrentPeriodStart()
		{
			return currentPeriodStart;
		}

		public void setCurrentPeriodStart(Long currentPeriodStart)
		{
			this.currentPeriodStart = currentPeriodStart;
		}

		public Long getCurrentPeriodEnd()
		{
			return currentPeriodEnd;
		}

		public void setCurrentPeriodEnd(Long currentPeriodEnd)
		{
			this.currentPeriodEnd = currentPeriodEnd;
		}

		public String getCardLast4()
		{
			return cardLast4;
		}

		public void setCardLast4(String cardLast4)
		{
			this.cardLast4 = cardLast4;
		}	
		
	}
	

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SubscriptionUser
	{
		private String id;
		private Map<String, Attribute> attributesByName;
		
		@JsonCreator
		public SubscriptionUser()
		{
		}

		@JsonIgnore
		public boolean isAdmin()
		{
			Attribute attr = attributesByName.get(SUBSCRIPTION_APP_ROLE);
			if (attr != null && attr.values.size() > 0 && attr.values.get(0).toLowerCase().equals("admin"))
				return true;
			return false;
		}

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public Map<String, Attribute> getAttributesByName()
		{
			return attributesByName;
		}

		public void setAttributesByName(Map<String, Attribute> attributesByName)
		{
			this.attributesByName = attributesByName;
		}

		@JsonIgnore
		public String getEmail()
		{
			Attribute attr = attributesByName.get(EMAIL_ATTR);
			if (attr != null && attr.values.size() > 0)
				try
				{
					return MAPPER.readValue(attr.values.get(0), ObjectNode.class).get("value")
							.asText();
				} catch (JsonProcessingException e)
				{
					return "";
				}
			return "";
		}
		
		@JsonIgnore
		public String getFirstname()
		{
			Attribute attr = attributesByName.get(FIRSTNAME_ATTR);
			if (attr != null && attr.values.size() > 0)
			{
				return attr.values.get(0);
			}
			return "";
		}
		
		@JsonIgnore
		public String getSurname()
		{
			Attribute attr = attributesByName.get(SURNAME_ATTR);
			if (attr != null && attr.values.size() > 0)
			{
				return attr.values.get(0);
			}
			return "";
		}
		
		@JsonIgnore
		public String getName()
		{
			Attribute attr = attributesByName.get(NAME_ATTR);
			if (attr != null && attr.values.size() > 0)
			{
				return attr.values.get(0);
			}
			return "";
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SubscriptionBase
	{
		public final String id;
		public final String creatorId;
		public final String group;
		public final Map<String, Attribute> attributesByName;

		@JsonCreator
		public SubscriptionBase(@JsonProperty("id") String id, @JsonProperty("creatorId") String creatorId,  @JsonProperty("group") String group,
				@JsonProperty("attrs") Map<String, Attribute> attrs)
		{
			this.id = id;
			this.creatorId = creatorId;
			this.group = group;
			this.attributesByName = attrs == null ? Collections.unmodifiableMap(Collections.emptyMap())
					: Collections.unmodifiableMap(attrs);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Attribute
	{
		private String name;
		private String valueSyntax;
		private String groupPath;
		private List<String> values = Collections.emptyList();
		private String translationProfile;
		private String remoteIdp;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getValueSyntax()
		{
			return valueSyntax;
		}

		public void setValueSyntax(String valueSyntax)
		{
			this.valueSyntax = valueSyntax;
		}

		public String getGroupPath()
		{
			return groupPath;
		}

		public void setGroupPath(String groupPath)
		{
			this.groupPath = groupPath;
		}

		public List<String> getValues()
		{
			return values;
		}

		public void setValues(List<String> values)
		{
			this.values = values;
		}

		public String getTranslationProfile()
		{
			return translationProfile;
		}

		public void setTranslationProfile(String translationProfile)
		{
			this.translationProfile = translationProfile;
		}

		public String getRemoteIdp()
		{
			return remoteIdp;
		}

		public void setRemoteIdp(String remoteIdp)
		{
			this.remoteIdp = remoteIdp;
		}

		@JsonCreator
		public Attribute()
		{
			
		}

		
		
		
	}

	public class InvitationParam
	{
		public final String type = "REGISTRATION";
		public final String formId;
		public final long expiration;
		public final String contactAddress;
		public final Map<Integer, String> identities = new HashMap<>();
		public final Map<Integer, String> attributes = new HashMap<>();
		public final Map<Integer, String> groupSelections = new HashMap<>();

		InvitationParam(String formId, long expiration, String contactAddress)
		{
			this.formId = formId;
			this.expiration = expiration;
			this.contactAddress = contactAddress;
		}
	}

	
	
	
	
	
}
