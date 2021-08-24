package com.codeapp.blog.exception;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.codeapp.blog.controller.AdminController;

public class AppExceptionControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	AdminController adminController;
	
	@InjectMocks
	AppExceptionController expController;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = standaloneSetup(expController, adminController).build();
		
		assertNotNull("mockMvc is null", mockMvc);
		
	}
	
	/** Test generic method for all other exceptions
	 * @throws Exception
	 */
/*	@Test
	public void testHandleNoHandlerFoundException() throws Exception {
		
		//userid was expected in following URL instead of test, should return MethodArgumentTypeMismatchException
		MvcResult result = 	mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/test"))
		.andExpect(status().isNotFound())
		.andDo(print())		
		.andReturn();
		
		assertTrue(result.getResolvedException() instanceof NoHandlerFoundException);
	}
	*/
	
	/** Test generic method for all other exceptions
	 * @throws Exception
	 */
	@Test
	public void testHandleExceptionInternal() throws Exception {
		
		//userid was expected in following URL instead of test, should return MethodArgumentTypeMismatchException
		MvcResult result = 
	mockMvc.perform(MockMvcRequestBuilders.get("/blog/admin/users/test"))
		.andExpect(status().isBadRequest())
		.andDo(print())		
		.andReturn();
		
		assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException);
	
	}

}
