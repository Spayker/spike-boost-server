package com.spikeboost.account.repository;

import com.spikeboost.account.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *  DAO layer for account model. Serves to exchange data between micro-service and related to it, db
 **/
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

	List<Account> findByName(String name);

	Account findByEmail(String email);

	List<Account> findByCreatedDate(Date createDate);

	List<Account> findByModifiedDate(Date modifiedDate);

	List<Account> findByAge(int age);

	List<Account> findByGender(int gender);

	List<Account> findByWeight(int weight);

	List<Account> findByHeight(int height);

}
