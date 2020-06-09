/*
 * Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.cloud.showcase;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SSLConfig
{
	@Autowired
	private Environment env;

	@PostConstruct
	private void configureSSL()
	{
		System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
		System.setProperty("javax.net.ssl.trustStorePassword",
				env.getProperty("server.ssl.trust-store-password"));
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
		{
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		});
	}
}
