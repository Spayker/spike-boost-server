package com.spayker.auth.service;

import com.spayker.auth.domain.User;

/**
 *  Service layer interface to provided API for work with User entity.
 **/
public interface UserService {

	/**
	 *  Creates a new user in db.
	 *  @param user - object to be persisted
	 **/
	void create(User user);

}
