package com.picpaysimplificado.domain.user;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private BigDecimal balance;

    private UserType userType;


}
