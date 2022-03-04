package org.h0110w.som.core.service.mapper;

import org.h0110w.som.core.model.user_account.UserAccount;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserAccountMapper extends AbstractMapper<UserAccount, UserAccountDto> {

}
