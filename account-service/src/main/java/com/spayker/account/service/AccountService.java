package com.spayker.account.service;

import com.spayker.account.domain.Account;
import com.spayker.account.domain.User;

/**
 *  Service layer interface to provided API for work with Account entity.
 **/
public interface AccountService {

	Account findByName(String accountName);

	Account findById(String accountId);

	Account create(User user);

	void saveChanges(String name, Account update);
}
