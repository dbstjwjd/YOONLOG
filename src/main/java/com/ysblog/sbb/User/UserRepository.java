package com.ysblog.sbb.User;

import com.ysblog.sbb.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);
    Optional<SiteUser> findByEmail(String email);
}
