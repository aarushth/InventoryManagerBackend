package com.leopardseal.demo;
import java.util.ArrayList;

public class UserTable {
    private ArrayList<User> users;
    public UserTable(){
        users = new ArrayList<User>(); 
    }
    public void addUser(User u){
        users.add(u);
    }
    public User findUser(String email){
        for(User u: users){
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
    }
}
