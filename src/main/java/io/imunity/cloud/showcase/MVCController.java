package io.imunity.cloud.showcase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MVCController
{
	@RequestMapping("/")
	public String main()
	{
		return "main";
	}
}
