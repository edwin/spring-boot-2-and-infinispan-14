package com.edw.repository;

import com.edw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 *  com.edw.repository.UserRepository
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 15 Oct 2024 19:42
 */
public interface UserRepository extends JpaRepository<User, String> {
}