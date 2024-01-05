package com.hositamtam.plypockets.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponseDTO {
    private String nickname;
    private Long userId;
}
