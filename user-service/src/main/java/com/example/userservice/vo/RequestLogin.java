package com.example.userservice.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogin {

    @NotNull(message = "email cannot be null.")
    @Size(min = 2, message = "email not be less then two characters")
    @Email
    private String email;

    @NotNull(message = "password cannot be null.")
    @Size(min = 8, message = "password must be equals or greater than eight characters")
    @Email
    private String password;



}
