package com.mediflow.emr.dto;

import lombok.Builder;

@Builder
public record EmailSignupRequest(
        String email,
        String password
) {
}
