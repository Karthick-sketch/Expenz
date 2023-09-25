package com.karthick.Expenz.repository;

import com.karthick.Expenz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
