package com.leopardseal.inventorymanager.entity.dto;

import com.leopardseal.inventorymanager.entity.MyUsers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private MyUsers user;
}