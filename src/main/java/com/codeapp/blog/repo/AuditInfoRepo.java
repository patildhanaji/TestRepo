package com.codeapp.blog.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeapp.blog.model.AuditInfo;

public interface AuditInfoRepo extends JpaRepository<AuditInfo, Long> {
	
	List<AuditInfo> findByUserId(long userId);
	List<AuditInfo> findByPostTitle(String title);
	List<AuditInfo> findByPostBody(String body);
}
