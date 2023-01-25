package com.pshpyro.sigma.security.time.entity;

import com.pshpyro.sigma.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class UserTimeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column
    private Date passwordChangedTime = new Date();

    @Column
    private Date rightsChangedTime = new Date();
}
