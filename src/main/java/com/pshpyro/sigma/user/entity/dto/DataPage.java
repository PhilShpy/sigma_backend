package com.pshpyro.sigma.user.entity.dto;

import java.util.List;

public record DataPage<T>(
        List<T> data,
        int page,
        int totalPages
) {
}
