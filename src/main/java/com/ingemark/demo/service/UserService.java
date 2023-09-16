package com.ingemark.demo.service;

import com.ingemark.demo.dto.PasswordChangeDTO;
import com.ingemark.demo.exception.IncorrectPasswordException;
import com.ingemark.demo.model.User;
import com.ingemark.demo.repository.UserRepository;
import com.ingemark.demo.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO, String requester) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error(Constants.USERNAME_NOT_FOUND);
                    return new UsernameNotFoundException(Constants.USERNAME_NOT_FOUND);
                });

        if (!passwordEncoder.matches(passwordChangeDTO.oldPassword(), user.getPassword())) {
            logger.error(Constants.OLD_PASSWORD_INCORRECT);
            throw new IncorrectPasswordException(Constants.OLD_PASSWORD_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.newPassword()));
        userRepository.save(user);

        if (!requester.equals(username)) {
            logger.info(Constants.ADMIN_CHANGED_PASSWORD_FOR_USER, requester, username);
        }
    }
}
