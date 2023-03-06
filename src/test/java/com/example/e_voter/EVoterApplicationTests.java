package com.example.e_voter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class EVoterApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testDatabaseConnection(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://127.0.0.1:3306", "root", "fannysammy53");

		try {
			Connection connection = dataSource.getConnection();
			assertThat(connection).isNotNull();
		} catch (SQLException exception){
			throw new RuntimeException(exception.getMessage());
		}
	}

}
