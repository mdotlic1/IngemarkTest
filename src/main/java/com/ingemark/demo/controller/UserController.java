package com.ingemark.demo.controller;

import com.ingemark.demo.dto.PasswordChangeDTO;
import com.ingemark.demo.exception.IncorrectPasswordException;
import com.ingemark.demo.service.UserService;
import com.ingemark.demo.util.Constants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{username}/change-password")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<String> changePassword(
            @PathVariable String username,
            @RequestBody @Valid PasswordChangeDTO passwordChangeDTO,
            Authentication authentication) {

        logger.info(Constants.PASSWORD_CHANGE_REQUEST, username);
        String requester = authentication.getName();

        try {
            userService.changePassword(username, passwordChangeDTO, requester);
            logger.info(Constants.PASSWORD_CHANGE_SUCCESS, username);
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException | IncorrectPasswordException e) {
            logger.error(Constants.PASSWORD_CHANGE_ERROR, username, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            logger.error(Constants.PASSWORD_CHANGE_UNEXPECTED_ERROR, username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.UNEXPECTED_ERROR);
        }
    }
}
