package com.api.parkingcontrol;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParkingControlApplicationTests {
	@AfterEach
	void tearDown() {
		System.out.println("After");
	}
	@AfterAll
	static void afterAll() {
		System.out.println("AfterAll");
	}
}
