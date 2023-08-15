package com.mogak.spring.service;

import com.mogak.spring.converter.UserConverter;
import com.mogak.spring.domain.user.Address;
import com.mogak.spring.domain.user.Job;
import com.mogak.spring.domain.user.User;
import com.mogak.spring.exception.ErrorCode;
import com.mogak.spring.exception.UserException;
import com.mogak.spring.login.JwtTokenProvider;
import com.mogak.spring.repository.AddressRepository;
import com.mogak.spring.repository.JobRepository;
import com.mogak.spring.repository.UserRepository;
import com.mogak.spring.util.Regex;
import com.mogak.spring.web.dto.UserRequestDto;
import com.mogak.spring.web.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AddressRepository addressRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public User create(UserRequestDto.CreateUserDto response) {
        inputVerify(response);
        Job job = jobRepository.findJobByName(response.getJob())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_JOB));
        Address address = addressRepository.findAddressByName(response.getAddress())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_ADDRESS));
        return userRepository.save(UserConverter.toUser(response, job, address));
    }
    @Override
    public Boolean findUserByNickname(String nickname) {
        if (userRepository.findOneByNickname(nickname).isPresent()) {
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }
        return false;
    }

    protected void inputVerify(UserRequestDto.CreateUserDto response) {
        if (!Regex.USER_NICKNAME_REGEX.matchRegex(response.getNickname(), "NICKNAME"))
            throw new UserException(ErrorCode.NOT_VALID_NICKNAME);
        if (!Regex.EMAIL_REGEX.matchRegex(response.getEmail(), "EMAIL"))
            throw new UserException(ErrorCode.NOT_VALID_EMAIL);
        findUserByNickname(response.getNickname());
    }

    public Boolean verifyNickname(String nickname) {
        if (!Regex.USER_NICKNAME_REGEX.matchRegex(nickname, "NICKNAME"))
            throw new UserException(ErrorCode.NOT_VALID_NICKNAME);
        return true;
    }

    @Override
    public User findUserByEmail(String email) {
        verifyEmail(email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_EXIST_USER));
    }

    protected void verifyEmail(String email) {
        if (!Regex.EMAIL_REGEX.matchRegex(email, "EMAIL"))
            throw new UserException(ErrorCode.NOT_VALID_EMAIL);
    }

    @Override
    public UserResponseDto.LoginDto getLoginDto(User user) {
        return UserConverter.toLoginDto(jwtTokenProvider.createJwtToken(user.getId().toString()));
    }

}
