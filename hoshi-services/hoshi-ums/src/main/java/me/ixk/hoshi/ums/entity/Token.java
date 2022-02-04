/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.response.TokenView;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * 令牌表
 *
 * @author Otstar Lin
 * @date 2021/6/4 12:40
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "Token 表")
@Entity
@Table(name = "token", indexes = {@Index(name = "idx_token_token_unq", columnList = "token", unique = true)})
public class Token {

    @Id
    @Schema(name = "Token ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "Token")
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Schema(name = "名称")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Schema(name = "用户")
    @ToString.Exclude
    @JsonBackReference
    private User user;

    public TokenView toView() {
        return TokenView.builder().id(this.id).name(this.name).token(this.token).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Token token1 = (Token) o;
        return token != null && Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
