package com.hositamtam.plypockets.service;

import com.hositamtam.plypockets.domain.User;
import com.hositamtam.plypockets.dto.LoginDto;
import com.hositamtam.plypockets.dto.LoginErrorDTO;
import com.hositamtam.plypockets.dto.LoginResponseDTO;
import com.hositamtam.plypockets.dto.UserDto;
import com.hositamtam.plypockets.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final PlaylistService playlistService;
    private final UserRepository userRepository;


    @Autowired
    public UserService(PlaylistService playlistService, UserRepository userRepository) {
        this.playlistService = playlistService;
        this.userRepository = userRepository;
    }

    public User registerUser(UserDto userDto) {
        User newUser = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .nickname(userDto.getNickname())
                .build();

        return userRepository.save(newUser);
    }


    public String getPasswordByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(User::getPassword).orElse(null);
    }

    @Getter
    public static class Response {
        private Long userId;
        private String status;
        private String error;

        public Response(Long userId, String status) {
            this.userId = userId;
            this.status = status;
        }

        public Response(Long userId, String status, String error) {
            this.userId = userId;
            this.status = status;
            this.error = error;
        }
    }
    public ResponseEntity<?> loginUser(LoginDto loginRequest) {
        Optional<User> userOptional = userRepository.findByNickname(loginRequest.getNickname());

        if (loginRequest.getNickname().length() < 2 || loginRequest.getNickname().length() > 8) {
            LoginErrorDTO loginErrorDTO = createLoginErrorDTO("닉네임 글자수는 2자 이상 8자 이하이어야 합니다.");

            return new ResponseEntity<>(loginErrorDTO, HttpStatus.CONFLICT);
        }

        if (loginRequest.getPassword().length() < 8 || !containsDigitAndLetter(loginRequest.getPassword())) {
            LoginErrorDTO loginErrorDTO = createLoginErrorDTO("비밀번호는 숫자와 영문자를 포함한 8자 이상이어야 합니다.");
            return new ResponseEntity<>(loginErrorDTO, HttpStatus.CONFLICT);
        }
        if (!userOptional.isPresent()) {
            //위의조건 만족&DB에 해당 닉네임이 부존재시 등록후 자동로그인
            User newUser = saveNewUser(loginRequest);
            // 첫 선물 등록
            playlistService.createFirstPlaylist(newUser);
            LoginResponseDTO loginResponseDTO = createLoginResponseDTO(newUser);

            return new ResponseEntity<>(loginResponseDTO,HttpStatus.CREATED);
        }

        if (userOptional.isPresent() && !userOptional.get().getPassword().equals(loginRequest.getPassword())) {
            LoginErrorDTO loginErrorDTO = createLoginErrorDTO("비밀번호가 일치하지 않습니다.");
            return new ResponseEntity<>(loginErrorDTO, HttpStatus.FORBIDDEN);
        }



        // 해당 조건 모두 통과시 Statuscode 200 반환
        LoginResponseDTO loginResponseDTO = createLoginResponseDTO(userOptional.get());
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    private LoginErrorDTO createLoginErrorDTO(String error) {
        LoginErrorDTO loginErrorDTO = LoginErrorDTO.builder()
                .Error(error)
                .build();
        return loginErrorDTO;
    }

    private LoginResponseDTO createLoginResponseDTO(User newUser) {
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .nickname(newUser.getNickname())
                .userId(newUser.getUserId())
                .build();
        return loginResponseDTO;
    }

    private User saveNewUser(LoginDto loginRequest) {
        User user;
        user = new User();
        user.setNickname(loginRequest.getNickname());
        user.setPassword(loginRequest.getPassword());
        User newUser = userRepository.save(user);
        return newUser;
    }

    private boolean containsDigitAndLetter(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }
}
