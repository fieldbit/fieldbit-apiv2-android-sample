package com.myapp.api.model.auth.request;

import java.util.ArrayList;

public class ModifiedArrayUser {
    private ArrayList<ModifiedUserBody> users;

    public ModifiedArrayUser(ArrayList<ModifiedUserBody> users) {
        this.users = users;
    }

    public ArrayList<ModifiedUserBody> getUsers() {
        return users;
    }
}
