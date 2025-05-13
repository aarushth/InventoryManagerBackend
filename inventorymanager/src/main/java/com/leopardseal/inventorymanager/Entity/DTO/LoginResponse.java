package com.leopardseal.inventorymanager.Entity.DTO;

import com.leopardseal.inventorymanager.Entity.MyUsers;
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