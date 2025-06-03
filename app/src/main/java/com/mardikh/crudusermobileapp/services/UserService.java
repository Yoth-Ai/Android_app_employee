package com.mardikh.crudusermobileapp.services;

import com.mardikh.crudusermobileapp.models.Role;
import com.mardikh.crudusermobileapp.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    void insertUser(User user);
    void updateUser(User user);

    User getUserById(int id);

    List<Role> getAllRoles();

    Role getRoleById(int roleId);
}
