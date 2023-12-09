package com.imhero;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@ActiveProfiles(profiles = "test")
@Transactional
@SpringBootTest
class ImheroApplicationTests {

	@Test
	void contextLoads() {
	}

}
