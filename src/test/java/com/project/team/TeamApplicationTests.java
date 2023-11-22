package com.project.team;

import com.project.team.Restaurant.Restaurant;
import com.project.team.Restaurant.RestaurantRepository;
import com.project.team.Restaurant.RestaurantService;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TeamApplicationTests {
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private SiteUserRepository siteUserRepository;


	@Test
	void contextLoads() {

	}
}
