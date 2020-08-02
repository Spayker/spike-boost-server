package com.spikeboost.account.dto.mapper;

import com.spikeboost.account.domain.Account;
import com.spikeboost.account.domain.User;
import com.spikeboost.account.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account accountDtoToAccount(AccountDto accountDto);

    @Mapping(source = "email", target = "username")
    User accountDtoToUser(AccountDto accountDto);

}
