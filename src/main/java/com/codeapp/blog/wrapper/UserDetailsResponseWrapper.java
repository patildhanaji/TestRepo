package com.codeapp.blog.wrapper;

import com.codeapp.blog.model.UserDetails;
import com.codeapp.blog.model.UserPosts;

public class UserDetailsResponseWrapper {

	private UserDetails userDetails;
	private UserPosts[] userPost;
	
	public UserDetailsResponseWrapper(){
		
	}
	
	public UserDetailsResponseWrapper(UserDetails userDetails, UserPosts[] userPost) {
		super();
		this.userDetails = userDetails;
		this.userPost = userPost;
	}
	
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	public UserPosts[] getUserPost() {
		return userPost;
	}
	public void setUserPost(UserPosts[] userPost) {
		this.userPost = userPost;
	}
	
	
}
