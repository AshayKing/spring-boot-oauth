package io.github.ashayking.service;

import java.util.Optional;

import javax.security.auth.login.AccountException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.ashayking.model.Account;
import io.github.ashayking.repository.AccountRepository;

/**
 * 
 * @author Ashay S Patil
 *
 */
@Service
public class AccountService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		Optional<Account> account = accountRepository.findByUsername(s);
		if (account.isPresent()) {
			return account.get();
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", s));
		}
	}

	public Account findAccountByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> account = accountRepository.findByUsername(username);
		if (account.isPresent()) {
			return account.get();
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
		}

	}

	public Account register(Account account) throws AccountException {
		if (accountRepository.countByUsername(account.getUsername()) == 0) {
			account.setPassword(passwordEncoder.encode(account.getPassword()));
			return accountRepository.save(account);
		} else {
			throw new AccountException(String.format("Username[%s] already taken.", account.getUsername()));
		}
	}

	@Transactional
	public void removeAuthenticatedAccount() throws UsernameNotFoundException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Account acct = findAccountByUsername(username);
		accountRepository.deleteAccountById(acct.getId());
	}
}
