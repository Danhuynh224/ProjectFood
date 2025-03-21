package com.example.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPassRequest {
    private String email;
    private String oldPass;
    private String newPass;
}
