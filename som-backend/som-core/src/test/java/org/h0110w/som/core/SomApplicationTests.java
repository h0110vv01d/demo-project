package org.h0110w.som.core;

import org.h0110w.som.core.common.AbstractTestWithDbAndKeycloak;
import org.h0110w.som.core.common.test_utils.Cleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SomApplicationTests extends AbstractTestWithDbAndKeycloak {
	@Autowired
	private Cleaner cleaner;

	@BeforeEach
	public void setup() {
		cleaner.deleteTasks();
		cleaner.deleteUsersFromKeycloakAndDB();
	}

	@Test
	void contextLoads() {
	}

}
