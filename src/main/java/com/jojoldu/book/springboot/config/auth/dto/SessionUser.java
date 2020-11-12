package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * Entity 클래스 대신 Dto 클래스를 생성해서 세션에 넣는 이유는 Serializable 때문이다.
 * Entity 클래스는 다른 Entity 클래스들 끼리 관계를 맺을 가능성이 많다 (@OneToMany, @ManyToMany 등등)
 * Entity 클래스에 Serializable 를 구현하면 직렬화 대상이 관계를 맺는 Entity 까지 포함이 되기 때문에 성능이슈나 부수효과가 날 가능성이 높아진다.
 * 그래스 직렬화 기능을 가진 세션 Dto 를 하나 추가로 만드는게 더 좋다.
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
