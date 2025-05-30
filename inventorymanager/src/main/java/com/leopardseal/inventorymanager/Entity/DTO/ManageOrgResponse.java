package com.leopardseal.inventorymanager.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ManageOrgResponse {
    
    private List<UserResponse> users;

    private List<UserResponse> invites;
}
