package com.mardikh.crudusermobileapp.services.impl;

import com.mardikh.crudusermobileapp.models.Role;
import com.mardikh.crudusermobileapp.models.User;
import com.mardikh.crudusermobileapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static List<User> userList = new ArrayList<>();
    private static List<Role> roleList = new ArrayList<>();
    @Override
    public List<User> getAllUser() {
        if (userList.isEmpty()){
            userList.add(new User(
                    1,"sok","sok@gmail.com","Male", getRoleById(1)
            ));
            userList.add(new User(
                    2,"san","san@gmail.com","Male", getRoleById(2)
            ));
            userList.add(new User(
                    3,"soksan","soksan@gmail.com","Female", getRoleById(3)
            ));
            userList.add(new User(
                    4,"Sensok","sansok@gmail.com","Male", getRoleById(4)
            ));
        }
        return userList;
    }

    @Override
    public void insertUser(User user) {
        user.setId(userList.size()+1);
        userList.add(user);

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public User getUserById(int id) {
        for (User user : userList){
            if (user.getId()==id){
                return user;
            }
        }
        return null;

    }

    @Override
    public List<Role> getAllRoles() {
        if (roleList.isEmpty()){
            Role roleAdmin = new Role(1, "Admin");
            roleList.add(roleAdmin);
            Role roleUser = new Role(2, "User");
            roleList.add(roleUser);
            Role roleCashier = new Role(3, "Cashier");
            roleList.add(roleCashier);
            Role roleSupperAdmin = new Role(4, "Super Admin");
            roleList.add(roleSupperAdmin);
        }

        return roleList;
    }

    @Override
    public Role getRoleById(int roleId) {
        for (Role role: roleList){
            if (role.getId()==roleId){
                return role;
            }
        }
        return null;
    }
}
