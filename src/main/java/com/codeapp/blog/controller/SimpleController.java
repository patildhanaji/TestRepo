package com.codeapp.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SimpleController {
	
	@RequestMapping(value="/blog", method = RequestMethod.GET)
	public String defaultPage(Model model){
		return "blog/index";
	}

	@RequestMapping(value="/blog/index", method = RequestMethod.GET)
	public String indexPage(Model model){
		return "blog/index";
	}
	
	/**
	 * Method added to create <i>POST</i> request for Post creation on behalf of user by Admin.
	 * @param model
	 * @return navigation for FORM jsp
	 */
	@RequestMapping(value="/blog/admin/users/createpost", method = RequestMethod.GET)
	public String createUserPost(Model model){
		
		return "blog/createPost";
	}
}
