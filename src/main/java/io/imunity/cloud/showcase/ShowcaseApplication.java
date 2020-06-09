/**************************************************************************
 *            Copyright (c) 2020 Bixbit - Krzysztof Benedyczak. 
 *                         All rights reserved.
 *                 See LICENCE file for licensing information.
 **************************************************************************/
package io.imunity.cloud.showcase;

import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Entry point.
 */
@SpringBootApplication
public class ShowcaseApplication
{
	public ShowcaseApplication(FreeMarkerConfigurer freeMarkerConfigurer)
	{
		freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(Arrays.asList("/META-INF/security.tld"));
	}

	public static void main(String[] args)
	{
		HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
		SpringApplication.run(ShowcaseApplication.class, args);
	}
}
