package com.spikeboost.account.util.factory;

import com.spikeboost.account.domain.Account;
import com.spikeboost.account.domain.Gender;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

public class AccountFactory {

    public static Account createDummyAccount(){
        return Account.builder()
                .name(RandomStringUtils.randomAlphabetic(10))
                .email(RandomStringUtils.randomAlphabetic(6) + "@somemail.com")
                .createdDate(new Date())
                .modifiedDate(null)
                .age(25)
                .gender(Gender.MALE)
                .weight(85)
                .height(185)
                .build();
    }

    public static Account createAccount(String name,
                                        String email,
                                        Date createDate,
                                        Date modifiedDate,
                                        int age,
                                        Gender male,
                                        int weight,
                                        int height) {
        return Account.builder()
                .name(name)
                .email(email)
                .createdDate(createDate)
                .modifiedDate(modifiedDate)
                .age(age)
                .gender(male)
                .weight(weight)
                .height(height)
                .build();
    }
}
