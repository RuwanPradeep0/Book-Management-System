package com.ruwan.BookNetwork.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponsse {
    private String token;
}
