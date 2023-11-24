package com.project.team.User;

import com.project.team.User.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByLoginId(String loginId);
    Optional<SiteUser> findByEmail(String email);

    Optional<SiteUser> findByToken(String token);

}
