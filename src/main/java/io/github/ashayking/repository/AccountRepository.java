package io.github.ashayking.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import io.github.ashayking.model.Account;

/**
 * 
 * @author Ashay S Patil
 *
 */
public interface AccountRepository extends Repository<Account, Long> {

	Collection<Account> findAll();

	Optional<Account> findByUsername(String username);

	Optional<Account> findById(Long id);

	Integer countByUsername(String username);

	Account save(Account account);

	void deleteAccountById(Long id);

}
