package com.spikeboost.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 *  Account model entity that contains major information about account.
 *  ToDo: update docs once model will be updated
 **/
@Document(collection = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

	@Id
	private String name;

	private Date lastSeen;

	private List<String> deviceIds;

}
