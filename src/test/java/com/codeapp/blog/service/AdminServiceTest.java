package com.codeapp.blog.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.codeapp.blog.model.AuditInfo;
import com.codeapp.blog.model.UserDetails;
import com.codeapp.blog.model.UserPosts;
import com.codeapp.blog.repo.AuditInfoRepo;
import com.codeapp.blog.wrapper.UserDetailsResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
		
	@Mock
	RestTemplateBuilder restTemplateBuilder;
	
	@Mock
	RestTemplate restTemplate;
		
	@Mock
	AuditInfoRepo auditRepo;
	
	@InjectMocks
	private AdminService adminService;
				
	private UserDetails userDetailsObj;
	private UserDetails[] userDetailsArr;
	
	private UserDetailsResponseWrapper wrapperObj1;	
	private List<UserDetailsResponseWrapper> wrapperObjList;
	
	private UserPosts userPostsObj1;
	private UserPosts[] userPostsObjArr;
	
	private AuditInfo auditInfoObj1;
	private List<AuditInfo> auditInfoObjList;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		
		wrapperObjList = new ArrayList<UserDetailsResponseWrapper>();
		
		//Mapper object
		ObjectMapper obj = new ObjectMapper();
				
		userDetailsObj = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/UserDetailsData.json"))),
				UserDetails.class);
		userDetailsArr = new UserDetails[1];
		userDetailsArr[0] = userDetailsObj;
		
		wrapperObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/UsersPostsData1.json"))),
				UserDetailsResponseWrapper.class);
		wrapperObjList = new ArrayList<UserDetailsResponseWrapper>();
		wrapperObjList.add(wrapperObj1);
				
		userPostsObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/PostsData1.json"))),
				UserPosts.class);
		userPostsObjArr = new UserPosts[1];
		userPostsObjArr[0] = userPostsObj1;
				
		auditInfoObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/AuditInfoData1.json"))),
				AuditInfo.class);
		auditInfoObjList = new ArrayList<AuditInfo>();
		auditInfoObjList.add(auditInfoObj1);
		
		//verify service and other mock objects				
		assertNotNull("adminService is null", adminService);
		assertNotNull("restTemplate is null", restTemplate);
		assertNotNull("auditRepo is null", auditRepo);
	}
	
	@Test
	public void testViewUserDetails(){		
		Mockito.when(restTemplate.getForObject(AdminService.API_USERS_URL,UserDetails[].class)).thenReturn(userDetailsArr);

		UserDetails[] userDetails = adminService.viewUserDetails();
		assertNotNull("userDetails is null", userDetails);		
	}
		
	@Test
	public void testViewAllPosts(){	
		Mockito.when(restTemplate.getForObject(AdminService.API_POSTS_URL,UserPosts[].class)).thenReturn(userPostsObjArr);

		UserPosts[] userPostsArr = adminService.viewAllPosts();
		assertNotNull("userPosts are null", userPostsArr);
		assertTrue(userPostsArr.length == 1);
	}
	
	@Test
	public void viewAllUserDetailsWithPosts(){
		Mockito.when(restTemplate.getForObject(AdminService.API_USERS_URL,UserDetails[].class)).thenReturn(userDetailsArr);
		Mockito.when(restTemplate.getForObject(AdminService.API_POSTS_URL,UserPosts[].class)).thenReturn(userPostsObjArr);
		
		List<UserDetailsResponseWrapper> wrapperObjList = adminService.viewAllUserDetailsWithPosts();
		assertNotNull("viewAllUserDetailsWithPosts is null", wrapperObjList);
	}
		
	@Test
	public void testViewAuditInfo(){				
		Mockito.when(auditRepo.findAll()).thenReturn(auditInfoObjList);
		
		List<AuditInfo> aduitInfoList = adminService.viewAuditInfo();
		assertNotNull("AuditInfo is null", aduitInfoList);		
	}
	
	@Test
	public void testCreateUserPost(){
		Mockito.when(restTemplate.postForObject(AdminService.API_POSTS_URL, HttpEntity.class, UserPosts.class)).thenReturn(userPostsObj1);
		
		UserPosts createdPostObj = adminService.createUserPost(userPostsObj1, "admin");
		assertTrue(null == createdPostObj);		
	}
	
}
