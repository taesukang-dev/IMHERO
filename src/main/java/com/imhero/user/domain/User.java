package com.imhero.user.domain;

import com.imhero.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private String delYn;

    private User(String email, String password, String username, String delYn) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.delYn = delYn;
    }

    public static User of(String email, String password, String username, String delYn) {
        return new User(email, password, username, delYn);
    }

    public User modify(String email, String password, String username, Role role) {
        if (StringUtils.hasText(email)) {
            this.email = email;
        }
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
        if (StringUtils.hasText(username)) {
            this.username = username;
        }
        if (!ObjectUtils.isEmpty(role)) {
            this.role = role;
        }
        return this;
    }

    public boolean withdraw() {
        if (this.delYn.equals("Y")) {
            return false;
        }

        this.delYn = "Y";
        return true;
    }

}
