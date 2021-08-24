package com.codeapp.blog.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.codeapp.blog.model.AuditInfo;
import com.codeapp.blog.model.UserDetails;
import com.codeapp.blog.model.UserDetailsResponseWrapper;
import com.codeapp.blog.model.UserPosts;
import com.codeapp.blog.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@WebMvcTest(AdminController.class)
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	AdminController adminController;
	
	@Mock
	private AdminService adminService;

	private UserDetails userDetailsObj;
	private List<UserDetails> userObjList;
	
	private UserDetailsResponseWrapper wrapperObj1;
	private UserDetailsResponseWrapper wrapperObj2;
	private List<UserDetailsResponseWrapper> wrapperObjList;
	
	private UserPosts userPostsObj1;
	private UserPosts userPostsObj2;
	private List<UserPosts> userPostsObjList;
	
	private AuditInfo auditInfoObj1;
	private AuditInfo auditInfoObj2;
	private List<AuditInfo> auditInfoObjList;

	
	// private static final String API_ROOT =
	// "http://localhost:8081/blog/admin/users";

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		//Initialise array lists
		userObjList = new ArrayList<UserDetails>();
		wrapperObjList = new ArrayList<UserDetailsResponseWrapper>();
		userPostsObjList = new ArrayList<UserPosts>();
		auditInfoObjList = new ArrayList<AuditInfo>();
			
		//Mapper object
		ObjectMapper obj = new ObjectMapper();
		
		//populate data objects and lists
		userDetailsObj = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/UserDetailsData.json"))),
				UserDetails.class);
		userObjList.add(userDetailsObj);
		
		wrapperObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/UsersPostsData1.json"))),
				UserDetailsResponseWrapper.class);
		wrapperObj2 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/UsersPostsData2.json"))),
				UserDetailsResponseWrapper.class);
		wrapperObjList.add(wrapperObj1);
		wrapperObjList.add(wrapperObj2);
		
		userPostsObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/PostsData1.json"))),
				UserPosts.class);
		userPostsObj2 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/PostsData2.json"))),
				UserPosts.class);
		userPostsObjList.add(userPostsObj1);
		userPostsObjList.add(userPostsObj2);
		
		auditInfoObj1 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/AuditInfoData1.json"))),
				AuditInfo.class);
		auditInfoObj2 = obj.readValue(
				new String(Files.readAllBytes(Paths.get("src/test/resources/AuditInfoData2.json"))),
				AuditInfo.class);
		auditInfoObjList.add(auditInfoObj1);
		auditInfoObjList.add(auditInfoObj2);
		
		//verify data object and list creation
		assertNotNull("userDetailsObj is null couldn't read from file", userDetailsObj);
		assertNotNull("wrapperObj1 is null couldn't read from file", wrapperObj1);
		assertNotNull("wrapperObj2 is null couldn't read from file", wrapperObj2);
		assertNotNull("userPostsObj1 is null couldn't read from file", userPostsObj1);
		assertNotNull("userPostsObj2 is null couldn't read from file", userPostsObj2);
		assertNotNull("auditInfoObj1 is null couldn't read from file", auditInfoObj1);
		assertNotNull("auditInfoObj2 is null couldn't read from file", auditInfoObj2);

		//verify service and controller
		assertNotNull("adminController is null", adminController);
		assertNotNull("adminService is null", adminService);

		//Inject mock objects
		mockMvc = standaloneSetup(adminController).build();
		assertNotNull("mockMvc is null", mockMvc);
	}

	@Test
	public void testViewUserDetails() throws Exception{

		Mockito.when(adminService.viewUserDetails()).thenReturn(userObjList.toArray(new UserDetails[0]));

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/users"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));		
	}

	@Test
	public void testViewUserDetailsByID() throws Exception{
		long userId = 1L;
		Mockito.when(adminService.viewUserDetailsByID(userId)).thenReturn(userDetailsObj);

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/users/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Leanne Graham")));
	}
	
	@Test
	public void testViewUserDetailsWithPosts() throws Exception {

		long userId = 1L;
		Mockito.when(adminService.viewUserDetailsWithPosts(userId)).thenReturn(wrapperObj1);

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/usersposts/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.userDetails.id", is(1)))
			.andExpect(jsonPath("$.userPost[0].userId", is(1)))
			.andExpect(jsonPath("$.userPost[0].title", is("it will be blinding")));
	}	
	
	@Test
	public void testViewAllUserDetailsWithPosts() throws Exception {
		
		Mockito.when(adminService.viewAllUserDetailsWithPosts()).thenReturn(wrapperObjList);

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/usersposts/allusers"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.[0].userDetails.id", is(1)))
			.andExpect(jsonPath("$.[1].userDetails.id", is(2)))
			.andExpect(jsonPath("$.[0].userPost[0].userId", is(1)))
			.andExpect(jsonPath("$.[1].userPost[1].userId", is(2)))
			.andExpect(jsonPath("$.[1].userPost[0].title", is("user two title 7")));
	}
	
	@Test
	public void testViewAllPosts() throws Exception{
		Mockito.when(adminService.viewAllPosts()).thenReturn(userPostsObjList.toArray((new UserPosts[0])));

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/posts"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testViewPostById() throws Exception{
		long postId = 1;
		Mockito.when(adminService.viewPostById(postId)).thenReturn(userPostsObj1);

		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/posts/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.userId", is(3)));
	}
	
	@Test
	public void testCreateUserPost() throws Exception{
		
		Mockito.when(adminService.viewAuditInfo()).thenReturn(auditInfoObjList);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/blog/admin/users/createpost")
				.param("userId", "1")
				.param("title", "TestTitle")
				.param("body", "TestBody")
				.param("loginUser", "TestUser"))					
			.andDo(print())
			.andExpect(status().isOk());						
	}
		
	@Test
	public void testViewAuditInfo() throws Exception{
		
		Mockito.when(adminService.viewAuditInfo()).thenReturn(auditInfoObjList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/audit"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.[0].id", is(1)))
			.andExpect(jsonPath("$.[0].userId", is(11)))
			.andExpect(jsonPath("$.[1].id", is(2)))
			.andExpect(jsonPath("$.[1].postTitle", is("title13")));
		
	}

}
