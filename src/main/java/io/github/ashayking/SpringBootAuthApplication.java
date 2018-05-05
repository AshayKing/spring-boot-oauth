package io.github.ashayking;

import java.util.Arrays;

import javax.security.auth.login.AccountException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.github.ashayking.model.Account;
import io.github.ashayking.service.AccountService;

/**
 * Ref : https://github.com/tinmegali/Oauth2-Stateless-Authentication-with-Spring-and-JWT-Token
 * 
 * @author Ashay S Patil
 *
 */
@SpringBootApplication
public class SpringBootAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAuthApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Qualifier("mainDataSource")
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.H2).build();
		return db;
	}

	@Bean
	CommandLineRunner init(AccountService accountService) {
		return (evt) -> Arrays.asList("user,admin,ashay".split(",")).forEach(username -> {
			Account acct = new Account();
			acct.setUsername(username);
			if (username.equals("user"))
				acct.setPassword("password");
			else
				acct.setPassword(passwordEncoder().encode("password"));
			acct.setFirstName(username);
			acct.setLastName("LastName");
			acct.grantAuthority("ROLE_USER");
			if (username.equals("admin"))
				acct.grantAuthority("ROLE_ADMIN");
			try {
				accountService.register(acct);
			} catch (AccountException e) {
				e.printStackTrace();
			}
		});
	}
}
