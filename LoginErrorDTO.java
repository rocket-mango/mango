package com.hositamtam.plypockets.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class LoginErrorDTO {
    private String Error;
}
