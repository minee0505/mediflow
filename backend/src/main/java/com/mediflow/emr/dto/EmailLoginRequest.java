package com.mediflow.emr.dto;

import lombok.Builder;

@Builder
public record EmailLoginRequest(
        String email,
        String password
) {
}

