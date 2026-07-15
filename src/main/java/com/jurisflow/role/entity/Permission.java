package com.jurisflow.role.entity;

import com.jurisflow.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {


    @Column(nullable = false, unique = true, length = 100)
    private String code;


    @Column(nullable = false)
    private String description;


    protected Permission() {
    }


    public Permission(
            String code,
            String description
    ) {

        this.code = code;
        this.description = description;

    }


    public String getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }

}