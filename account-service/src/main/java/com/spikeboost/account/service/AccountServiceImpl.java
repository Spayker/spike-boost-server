package com.spikeboost.account.service;

import com.spikeboost.account.client.AuthServiceClient;
import com.spikeboost.account.domain.Account;
import com.spikeboost.account.domain.User;
import com.spikeboost.account.exception.AccountException;
import com.spikeboost.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *  Service layer implementation to work with Account entities.
 **/
@Service
public class AccountServiceImpl implements AccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountRepository repository;

	@Autowired
	private AuthServiceClient authClient;

	@Override
	public List<Account> findAccountByName(String name) {
		if(name.isEmpty() || name.isBlank()){
			throw new IllegalArgumentException("provided name is empty or blank");
		}
		return repository.findByName(name);
	}

	@Override
	public Account findAccountByEmail(String email) {
		if(email.isEmpty() || email.isBlank()){
			throw new IllegalArgumentException("provided email is empty or blank");
		}
		return repository.findByEmail(email);
	}

	@Override
	public List<Account> findAccountByCreatedDate(Date createdDate) {
		return repository.findByCreatedDate(createdDate);
	}

	@Override
	public List<Account> findAccountByModifiedDate(Date modifiedDate) { return repository.findByModifiedDate(modifiedDate); }

	@Override
	public List<Account> findAccountByAge(int age) {
		return repository.findByAge(age);
	}

	@Override
	public List<Account> findAccountByGender(int gender) {
		return repository.findByGender(gender);
	}

	@Override
	public List<Account> findAccountByWeight(int weight) {
		return repository.findByWeight(weight);
	}

	@Override
	public List<Account> findAccountByHeight(int height) {
		return repository.findByHeight(height);
	}

	@Override
	public Account create(Account account, User user) {
		Account existing = repository.findByEmail(account.getEmail());
		if(existing == null){
			authClient.createUser(user);
			Account savedAccount = repository.save(account);
			log.info("new account has been created: " + savedAccount.getEmail());
			return savedAccount;
		} else {
			throw new AccountException("account already exists: " + account.getEmail());
		}
	}

	@Override
	public Account saveChanges(Account update) {
		Account account = repository.findByEmail(update.getEmail());
		if(account == null){
			throw new AccountException("can't find account with email " + update.getEmail());
		} else {
			update.setModifiedDate(new Date());
			log.debug("account {} changes have been saved", update.getEmail());
			return repository.save(update);
		}
	}
}
