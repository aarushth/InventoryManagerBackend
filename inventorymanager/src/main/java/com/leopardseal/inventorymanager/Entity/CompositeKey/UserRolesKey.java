package com.leopardseal.inventorymanager.Entity.CompositeKey;

import java.io.Serializable;
import java.util.Objects;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserRolesKey implements Serializable {

    private int user_id;

    private int org_id;

    
    public boolean equals(UserRolesKey checkKey){
        return (user_id == checkKey.user_id && org_id == checkKey.org_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user_id, this.org_id);
    }

}