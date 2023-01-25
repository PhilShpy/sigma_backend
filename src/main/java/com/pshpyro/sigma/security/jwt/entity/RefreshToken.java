package com.pshpyro.sigma.security.jwt.entity;

import com.pshpyro.sigma.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken { // TODO try to use record instead of class

    @Id
    @NotNull
    @Column
    private String value;
    @NotNull
    @Column
    private Date expiration;
    @NotNull
    @Column
    private Date issuedAt;
    @Column
    private boolean valid = true;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
