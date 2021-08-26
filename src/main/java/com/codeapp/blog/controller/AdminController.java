package com.codeapp.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeapp.blog.model.AuditInfo;
import com.codeapp.blog.model.UserDetails;
import com.codeapp.blog.wrapper.UserDetailsResponseWrapper;
import com.codeapp.blog.model.UserPosts;
import com.codeapp.blog.service.AdminService;

@RestController
@RequestMapping("blog/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;

	@GetMapping
	public String defaultMapping(){
		return "Welcome to admin page";
	}
	
	//User and their posts related methods
	/** Return all users details	
	 * @return Arrary of user object
	 */
	@GetMapping("/users")
	public UserDetails[] viewUserDetails()
	{
		return adminService.viewUserDetails();
	}
	
	/** Return user details by specif user 
	 * @param id - user id
	 * @return - User detail object
	 */
	@GetMapping("/users/{id}")
	public UserDetails viewUserDetailsByID(@PathVariable long id)
	{
		return adminService.viewUserDetailsByID(id);
	}
	
	/** Returns all posts for specified user 
	 * @param id - user id
	 * @return - Wrapper object having details for user and related posts
	 */
	@GetMapping("/usersposts/{id}")
	public UserDetailsResponseWrapper viewUserDetailsWithPosts(@PathVariable long id){
		return adminService.viewUserDetailsWithPosts(id);
	}
	
	/** Return all user details and their respective posts 
	 * @return List of Wrapper object containing user details and related posts
	 */
	@GetMapping("/usersposts/allusers")
	public List<UserDetailsResponseWrapper> viewAllUserDetailsWithPosts(){
		return adminService.viewAllUserDetailsWithPosts();
	}
	
	
	/**
	 * Create a post on behalf of user
	 * @param userId
	 * @param title
	 * @param body
	 * @param loginUser
	 * @return user post object 
	 */
	@PostMapping("/users/createpost")
	public UserPosts createUserPost(@RequestParam long userId, 
			@RequestParam String postTitle,
			@RequestParam String postBody,
			@RequestParam String loginUser){
				
		UserPosts newUserPost = new UserPosts(userId, postTitle, postBody); 
		return adminService.createUserPost(newUserPost, loginUser);	
		
	}
	
	// Posts related methods
	
	@GetMapping("/posts")
	public UserPosts[] viewAllPosts(){
		System.out.println("in ActionController defaultPage");
		return adminService.viewAllPosts();		
	}
	
	@GetMapping("/posts/{postId}")
	public UserPosts viewPostById(@PathVariable long postId){
		UserPosts post = adminService.viewPostById(postId);
		System.out.println("returned in findPostById:" + post.toString());		
		return post;
	}
	// Audit related methods
	
	/**
	 * View all audit information
	 * @return List of Audit Info object
	 */
	@GetMapping("/audit")
	public List<AuditInfo> viewAuditInfo(){
		return adminService.viewAuditInfo();
		//return "successful";
	}
		
	@GetMapping("/audit/{id}")
	public AuditInfo viewAuditInfoById(@PathVariable long id){
		return adminService.viewAuditInfoById(id);		
	}
		
	@GetMapping("/audit/user/{userid}")
	public List<AuditInfo> viewAuditInfoByUserId(@PathVariable long userid){
		return adminService.viewAuditInfoByUserId(userid);		
	}
	
	@GetMapping("/audit/posttitle/{postTitle}")
	public List<AuditInfo> viewAuditInfoByPostTitle(@PathVariable String postTitle){
		return adminService.viewAuditInfoByPostTitle(postTitle);		
	}
	
	@GetMapping("/audit/postbody/{postBody}")
	public List<AuditInfo> viewAuditInfoByPostBody(@PathVariable String postBody){
		return adminService.viewAuditInfoByPostBody(postBody);		
	}
	
}
