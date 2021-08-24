package com.codeapp.blog;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codeapp.blog.controller.AdminController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BlogApplicationTests {
	
	@Autowired
	private AdminController adminController;

	@Test
	public void contextLoads() {
		assertNotNull("Application unable to initialised.",adminController);
	}

}
