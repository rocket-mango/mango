package com.hositamtam.plypockets.domain;

import com.hositamtam.plypockets.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;

    @Column(unique = true)
    private String nickname;

}
