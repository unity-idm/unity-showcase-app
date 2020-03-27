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
	
	@RequestMapping("/tenant-selection")
	public String selectTenant()
	{
		return "tenant-selection";
	}
	
	@RequestMapping("/application")
	public String application()
	{
		return "application";
	}
}
