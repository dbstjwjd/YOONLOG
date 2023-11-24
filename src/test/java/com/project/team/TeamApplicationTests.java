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


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;



import com.project.team.Restaurant.RestaurantRepository;
import com.project.team.User.SiteUserRepository;
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





		Restaurant r1 = new Restaurant();
		r1.setName("떡반집");
		r1.setAddress("대전 서구 둔산로 8");
		r1.setNumber("042-472-2921");
		r1.setOwner(null);
		r1.setRegDate(LocalDateTime.now());
		r1.setMain("떡반");










		this.restaurantRepository.save(r1);


	}
}
