package org.h0110w.som.core.controller;

import org.h0110w.som.core.controller.request.CreateUserAccountRequestDto;
import org.h0110w.som.core.controller.request.UpdateUserRequestDto;
import org.h0110w.som.core.model.user_account.UserAccountDto;
import org.h0110w.som.core.service.UserAccountsService;
import org.h0110w.som.core.service.util.pagination.CustomPageRequest;
import org.h0110w.som.core.service.util.pagination.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * api for managing users
 * accessible only for users with admin role
 */
@RestController
@RequestMapping(UserAccountsController.URL)
public class UserAccountsController {
    public static final String URL = "/user-accounts";

    @Autowired
    private UserAccountsService userService;

    /**
     * creates user
     *
     * @param request dto that contains data about login, password and role of user
     * @return UserDto dto that contains data about created user
     */
    @PostMapping("/create")
    public UserAccountDto create(@Valid @RequestBody CreateUserAccountRequestDto request) {
        return userService.createFromDto(request);
    }

    /**
     * returns list of users with UserRole.LOCAL
     *
     * @param pageRequest used for pagination/sorting/filtering
     * @return wrapped list of users
     */
    @PostMapping("/paged")
    public @ResponseBody
    PagedResult<UserAccountDto> paged(@RequestBody CustomPageRequest pageRequest) {
        return userService.paged(pageRequest);
    }

    /**
     * updates user
     *
     * @param request dto that contains data needed to updated a user
     * @return userDto contains data about updated user
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserAccountDto update(@RequestBody @Valid UpdateUserRequestDto request) {
        return userService.updateByDto(request);
    }

    /**
     * disables user
     *
     * @param userAccountDto dto containing userId to disable
     */
    @DeleteMapping("/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disable(@RequestBody UserAccountDto userAccountDto) {
        userService.disable(userAccountDto.getId());
    }
}
