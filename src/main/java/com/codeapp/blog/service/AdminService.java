package com.codeapp.blog.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.codeapp.blog.model.AuditInfo;
import com.codeapp.blog.model.UserDetails;
import com.codeapp.blog.model.UserDetailsResponseWrapper;
import com.codeapp.blog.model.UserPosts;
import com.codeapp.blog.repo.AuditInfoRepo;

@Service
public class AdminService {
		
	public static final String API_POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
	public static final String API_POSTS_URL_Param_ID = "https://jsonplaceholder.typicode.com/posts/{pid}";
		
	public static final String API_USERS_URL = "https://jsonplaceholder.typicode.com/users";
	public static final String API_USERS_URL_PARAM_ID = "https://jsonplaceholder.typicode.com/users/{uid}";
	
	public static final String API_USERS_POSTS_URL_PARAM_ID = "https://jsonplaceholder.typicode.com/users/{uid}/posts";
	
	private final RestTemplate restTemplate;
	
	@Autowired
	private AuditInfoRepo auditRepo;
	
	//autowired was not required so commented..works with or without
	//@Autowired
	/**
	 * Set up RestTemplate
	 * @param restTemplateBuilder
	 */
	public AdminService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
	
	/** 
	 * Return all users details
	 * @return Array of UserDetails object
	 */
	public UserDetails[] viewUserDetails(){
		// fetch response in a string object
		return restTemplate.getForObject(API_USERS_URL, UserDetails[].class);
		
	}
	
	/** Return details for specified user 
	 * @param userId - user id
	 * @return Object of UserDetails
	 */
	public UserDetails viewUserDetailsByID(long userId){
		// fetch response in a string object
		return restTemplate.getForObject(API_USERS_URL_PARAM_ID, UserDetails.class, new Object[] {userId});
		
	}
	
	/** Returns all posts for specified user
	 * This method gets posts only for the particular user using the parameterised API
	 * @param userId - user id
	 * @return Wrapper for User Details and related posts
	 */
	public UserDetailsResponseWrapper viewUserDetailsWithPosts(long userId){
		UserDetailsResponseWrapper wrapper = null;
		// fetch response in a string object
		UserDetails userDetails = restTemplate.getForObject(API_USERS_URL_PARAM_ID, UserDetails.class, new Object[]{userId});
		
		UserPosts[] posts = restTemplate.getForObject(API_USERS_POSTS_URL_PARAM_ID, UserPosts[].class, new Object[]{userId});
		
		if(null !=userDetails && null != posts){
			wrapper = new UserDetailsResponseWrapper(userDetails, posts);			
		} 
		
		return wrapper;
	}
	
	/** Return all user details and their respective posts
	 * This method gets all user Details and posts separately and then merge together based on matching user id
	 * @return List of Wrapper objects contacting user and their respective posts
	 */
	public List<UserDetailsResponseWrapper> viewAllUserDetailsWithPosts(){
		List<UserDetailsResponseWrapper> wrapperList = new ArrayList<UserDetailsResponseWrapper>(10);;
		UserDetailsResponseWrapper usrDtlWrapper= null;
		boolean validUsers = false;
		boolean validPosts = false;
		long userId = 0;
		List<UserPosts> userPostList = null;
		
		// fetch response in a string object
		UserDetails[] userDetails = restTemplate.getForObject(API_USERS_URL, UserDetails[].class);
		validUsers = null != userDetails && userDetails.length>0;
		
		UserPosts[] userPosts = restTemplate.getForObject(API_POSTS_URL, UserPosts[].class);
		validPosts = null != userPosts && userPosts.length>0;
		
		usrDtlWrapper= new UserDetailsResponseWrapper();
		usrDtlWrapper.setUserDetails(userDetails[0]);
		
		if(validUsers){
			for(UserDetails usrDtObj : userDetails){
				usrDtlWrapper= new UserDetailsResponseWrapper();
				usrDtlWrapper.setUserDetails(usrDtObj);
				userId = usrDtObj.getId();
				if(validPosts){
					userPostList = new ArrayList<UserPosts>();
					for(UserPosts pstObj: userPosts){
						if(userId == pstObj.getUserId()){
							userPostList.add(pstObj);
						}
					}
					usrDtlWrapper.setUserPost(userPostList.toArray(new UserPosts[0]));
				}				
				wrapperList.add(usrDtlWrapper);			
			}			
		}				
		return wrapperList;
	}
	
	//Post related methods
	
	/**
	 * Return all posts for all users
	 * @return Array of UserPosts object
	 */
	public UserPosts[] viewAllPosts(){
		// fetch response in a string object		 
		 UserPosts[] userPosts = restTemplate.getForObject(API_POSTS_URL, UserPosts[].class);
		
		 return userPosts;
	}
	
	/**
	 * All posts for the particular user
	 * @param id - post id
	 * @return UserPosts object
	 */
	public UserPosts viewPostById(long postId){
		UserPosts postObj = restTemplate.getForObject(API_POSTS_URL_Param_ID, UserPosts.class,new Object[] {postId});
		//return post[0];
		return postObj;
	}
		
	/** Create post on behalf of admin, and populate audit details
	 * @param newUserPost - Object of new Post
	 * @param loginUser - Logged in Admin user
	 * @return - Saved Post object 
	 */
	public UserPosts createUserPost(UserPosts newUserPost, String loginUser){
		UserPosts createdUserPostObj = null;
		AuditInfo savedAuditInfoObj = null;
	
		// create a post object
		//UserPosts newUserPost = new UserPosts(10,"title","book");
		
		HttpHeaders headers = new HttpHeaders();
	    // set `content-type` header
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    // set `accept` header
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	   
	    // build the request
	    HttpEntity<UserPosts> entity = new HttpEntity<>(newUserPost, headers);
		
	    try{
	    	createdUserPostObj = restTemplate.postForObject(API_POSTS_URL, entity, UserPosts.class);
		}catch (RestClientException rce){
	    	savedAuditInfoObj = saveAuditInfo(newUserPost, loginUser, "failed", rce.getMessage());
	    	throw rce;
	    }
	    	    
	    //save to audit info
	    if (null!= createdUserPostObj){
	    	saveAuditInfo(createdUserPostObj, loginUser, "success", null);
	    }else{
	    	saveAuditInfo(newUserPost, loginUser, "failed", "Unable to create a new post");
	    }
	    	    
	    return createdUserPostObj;
	}
		
	private AuditInfo saveAuditInfo(UserPosts userPostObj, String loginUser,
			String status, String errDesc){
		
		AuditInfo auditInfoObj = new AuditInfo(userPostObj.getUserId(),
				userPostObj.getId(),userPostObj.getTitle(),userPostObj.getBody(),
				status, errDesc,
	    		loginUser, new Timestamp(new Date().getTime()));
				
		return auditRepo.save(auditInfoObj);	   
	}

	//Audit related methods
	
	/**
	 * All audit info
	 * @return List of AuditInfo object
	 */
	public List<AuditInfo> viewAuditInfo(){
		return auditRepo.findAll();			
	}
	
	/**
	 * Audit info for particular audit id
	 * @param id - audit id
	 * @return - AuditInfo object
	 */
	public AuditInfo viewAuditInfoById(long id){
		return auditRepo.findOne(id);				
	}
	
	/**
	 * Audit infor for provided user
	 * @param userId - user id
	 * @return - List of AuditInfo object
	 */
	public List<AuditInfo> viewAuditInfoByUserId(long userId){		
		return auditRepo.findByUserId(userId);					
	}
	
	public List<AuditInfo> viewAuditInfoByPostTitle(String title){
		return auditRepo.findByPostTitle(title);					
	}
	
	public List<AuditInfo> viewAuditInfoByPostBody(String body){		
		return auditRepo.findByPostBody(body);	
	}
}
