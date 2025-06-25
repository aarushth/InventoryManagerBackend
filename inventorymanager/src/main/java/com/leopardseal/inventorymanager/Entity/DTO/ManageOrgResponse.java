package com.leopardseal.inventorymanager.entity.dto;

import java.util.List;

import com.leopardseal.inventorymanager.entity.Invite;
import com.leopardseal.inventorymanager.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ManageOrgResponse {
    
    private List<UserRole> users;

    private List<Invite> invites;
}
