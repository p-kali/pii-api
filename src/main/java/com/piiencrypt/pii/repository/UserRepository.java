package com.piiencrypt.pii.repository;

import com.piiencrypt.pii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
