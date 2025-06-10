package com.naz1k1.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@TableName("users")
@Data
public class User extends BaseEntity {
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String avatar;
    private String salt;
    private LocalDateTime lastSeen;
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
