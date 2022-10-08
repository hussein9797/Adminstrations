package com.accumed.Admini.enums;

public enum UserPermission {
    ADD_USER("ADD:add"),
    DELETE_USER("DELETE:delete"),
    UPDATE_USER("update_user"),
    READ_USER("user_read");



    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public  String getPermission(){
        return permission;
    }
}
