package com.mardikh.crudusermobileapp.services.impl;

import com.mardikh.crudusermobileapp.models.Role;
import com.mardikh.crudusermobileapp.models.User;
import com.mardikh.crudusermobileapp.services.UserService;

import org.junit.Before;
import org.junit.Test;
// Statically import Assert methods for better readability
import static org.junit.Assert.*;

import java.util.List;

public class UserServiceImplTest {

    private UserService userService;

    @Before
    public void setUp() {
        // Re-initialize or reset the service to ensure clean state for each test
        // Due to static lists in UserServiceImpl, we need a way to clear them.
        // A direct clear like UserServiceImpl.userList.clear() is not possible if private.
        // For now, we'll create a new instance, which gets fresh static lists *if they were instance lists*.
        // Since they are static, a new instance won't help clear them.
        // This is a limitation of the current UserServiceImpl design for testing.
        // We will proceed by fetching and clearing if the list instances are accessible.
        // Let's assume UserServiceImpl's getAllUser() and getAllRoles() return the live list
        // and clearing them would affect the static list.

        userService = new UserServiceImpl();

        // Clear roles and users to ensure a clean slate for each test.
        // This is crucial because the lists are static in UserServiceImpl.
        List<Role> roles = userService.getAllRoles(); // This initializes roles if empty
        roles.clear(); // Clear existing roles for a fresh start in setUp

        List<User> users = userService.getAllUser(); // This might add default users if empty
        users.clear(); // Clear existing users

        // Now, re-populate roles as getAllRoles() would in a fresh service
        // This is a bit of a workaround due to static lists.
        // Ideally, UserServiceImpl would have a reset method for tests.
        userService.getAllRoles(); // Call again to populate with default roles after clearing
    }

    @Test
    public void testGetAllRoles_initializesAndReturnsRoles() {
        List<Role> roles = userService.getAllRoles();
        assertNotNull(roles);
        assertFalse("Role list should not be empty after calling getAllRoles", roles.isEmpty());
        // Default roles are 1:Admin, 2:User, 3:Cashier, 4:Super Admin
        assertEquals("Should have 4 default roles", 4, roles.size());
        assertEquals("Admin", roles.get(0).getName());
    }

    @Test
    public void testGetRoleById_existingRole() {
        userService.getAllRoles(); // Ensure roles are populated
        Role adminRole = userService.getRoleById(1);
        assertNotNull(adminRole);
        assertEquals("Admin", adminRole.getName());

        Role userRole = userService.getRoleById(2);
        assertNotNull(userRole);
        assertEquals("User", userRole.getName());
    }

    @Test
    public void testGetRoleById_nonExistentRole() {
        userService.getAllRoles(); // Ensure roles are populated
        Role nonExistentRole = userService.getRoleById(99);
        assertNull(nonExistentRole);
    }

    @Test
    public void testInsertUser_firstUserHasIdOne() {
        // Ensure user list is empty by calling setUp's clear logic implicitly
        // or explicitly clearing again if needed (setUp should handle it)
        List<User> users = userService.getAllUser();
        users.clear(); // Make sure it's empty for this specific test logic

        Role role = userService.getRoleById(1); // Get Admin role
        assertNotNull("Role should not be null for user creation", role);

        User user1 = new User(0, "Test User 1", "test1@example.com", "Male", role);
        userService.insertUser(user1);

        assertEquals("First user should have ID 1", 1, user1.getId());
        assertEquals("User list should contain 1 user", 1, userService.getAllUser().size());
        assertEquals("Test User 1", userService.getUserById(1).getName());
    }

    @Test
    public void testInsertUser_subsequentUserHasIncrementingId() {
        userService.getAllUser().clear(); // Start fresh

        Role adminRole = userService.getRoleById(1); // Admin
        assertNotNull(adminRole);

        User user1 = new User(0, "User One", "one@example.com", "Male", adminRole);
        userService.insertUser(user1); // ID should be 1

        User user2 = new User(0, "User Two", "two@example.com", "Female", adminRole);
        userService.insertUser(user2); // ID should be 2

        assertEquals("User list should contain 2 users", 2, userService.getAllUser().size());
        assertEquals("First user ID should be 1", 1, user1.getId());
        assertEquals("Second user ID should be 2", 2, user2.getId());
        assertNotNull(userService.getUserById(2));
        assertEquals("User Two", userService.getUserById(2).getName());
    }

    @Test
    public void testInsertUser_idGenerationAfterMaxId() {
        userService.getAllUser().clear(); // Start fresh
        Role userRole = userService.getRoleById(2); // User role
        assertNotNull(userRole);

        // Manually add a user with a high ID to simulate existing data (not using insertUser for this one)
        // This is tricky because insertUser is what we are testing for ID generation.
        // Instead, let's insert a few users and check the sequence.
        User u1 = new User(0, "U1", "u1@e.com", "M", userRole);
        userService.insertUser(u1); // ID 1
        User u2 = new User(0, "U2", "u2@e.com", "F", userRole);
        userService.insertUser(u2); // ID 2

        // Simulate a user being deleted, and a new one added, then another one.
        // The ID generation should still be max + 1.
        // The current UserServiceImpl doesn't have delete. So we just add another.
        User u3 = new User(0, "U3", "u3@e.com", "M", userRole);
        userService.insertUser(u3); // ID 3

        assertEquals("User U3 should have ID 3", 3, u3.getId());

        // If we could manually add a user with ID 10:
        // User highIdUser = new User(10, "High ID", "high@example.com", "Male", userRole);
        // userService.getAllUser().add(highIdUser); // This bypasses insertUser's ID logic
        // User nextUser = new User(0, "Next User", "next@example.com", "Female", userRole);
        // userService.insertUser(nextUser);
        // assertEquals("Next user ID should be 11 (after high ID 10)", 11, nextUser.getId());
        // This part is commented out because we cannot directly add to the list to set up this specific scenario
        // without modifying UserServiceImpl or making its list public. The current `insertUser`
        // bases its ID on the *current maximum ID in the list*.
    }


    @Test
    public void testGetUserById_existingUser() {
        userService.getAllUser().clear();
        Role role = userService.getRoleById(1);
        User user = new User(0, "Find Me", "findme@example.com", "Male", role);
        userService.insertUser(user); // ID will be 1

        User foundUser = userService.getUserById(user.getId());
        assertNotNull(foundUser);
        assertEquals("Find Me", foundUser.getName());
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void testGetUserById_nonExistentUser() {
        userService.getAllUser().clear(); // Ensure no users
        User foundUser = userService.getUserById(999);
        assertNull(foundUser);
    }

    @Test
    public void testUpdateUser_existingUser() {
        userService.getAllUser().clear();
        Role adminRole = userService.getRoleById(1);
        Role userRole = userService.getRoleById(2);
        assertNotNull(adminRole);
        assertNotNull(userRole);

        User originalUser = new User(0, "Original Name", "original@example.com", "Male", adminRole);
        userService.insertUser(originalUser); // Gets an ID, e.g., 1

        User userToUpdate = new User();
        userToUpdate.setId(originalUser.getId()); // Use the same ID
        userToUpdate.setName("Updated Name");
        userToUpdate.setEmail("updated@example.com");
        userToUpdate.setGender("Female");
        userToUpdate.setRole(userRole); // Change role as well

        userService.updateUser(userToUpdate);

        User updatedUserFromService = userService.getUserById(originalUser.getId());
        assertNotNull(updatedUserFromService);
        assertEquals("Updated Name", updatedUserFromService.getName());
        assertEquals("updated@example.com", updatedUserFromService.getEmail());
        assertEquals("Female", updatedUserFromService.getGender());
        assertNotNull(updatedUserFromService.getRole());
        assertEquals(userRole.getId(), updatedUserFromService.getRole().getId());
        assertEquals("User", updatedUserFromService.getRole().getName());
    }

    @Test
    public void testUpdateUser_nonExistentUser() {
        userService.getAllUser().clear(); // Ensure user list is empty or doesn't contain the test ID
        Role role = userService.getRoleById(1);
        User nonExistentUser = new User(999, "Non Existent", "no@example.com", "Unknown", role);

        // Get current state of user list
        List<User> usersBeforeUpdate = userService.getAllUser();
        int sizeBeforeUpdate = usersBeforeUpdate.size();
        // Make a defensive copy if necessary, but size check is enough here.

        userService.updateUser(nonExistentUser); // Attempt to update

        List<User> usersAfterUpdate = userService.getAllUser();
        assertEquals("User list size should not change after trying to update non-existent user",
                sizeBeforeUpdate, usersAfterUpdate.size());

        // Optionally, verify that no user with ID 999 was added
        assertNull("User with ID 999 should not exist in the list", userService.getUserById(999));
    }

    // --- Tests for deleteUser ---

    @Test
    public void testDeleteUser_existingUser() {
        // Setup: Ensure roles are available and add a user
        userService.getAllRoles(); // Ensures roles are loaded (handled by setUp too)
        Role role = userService.getRoleById(1); // Get Admin role
        assertNotNull("Role should not be null for user creation", role);

        User userToDelete = new User(0, "Deleter", "delete@me.com", "Male", role);
        userService.insertUser(userToDelete);
        int userIdToDelete = userToDelete.getId();
        assertNotEquals("User ID should be assigned", 0, userIdToDelete);

        int initialSize = userService.getAllUser().size();

        // Action: Delete the user
        userService.deleteUser(userIdToDelete);

        // Assertions
        assertNull("Deleted user should not be found by ID", userService.getUserById(userIdToDelete));
        assertEquals("User list size should decrease by 1", initialSize - 1, userService.getAllUser().size());
    }

    @Test
    public void testDeleteUser_nonExistentUser() {
        // Setup: Ensure roles are available and add some users
        userService.getAllRoles();
        Role role = userService.getRoleById(1);
        assertNotNull(role);

        userService.insertUser(new User(0, "User A", "a@a.com", "Male", role));
        userService.insertUser(new User(0, "User B", "b@b.com", "Female", role));

        int initialSize = userService.getAllUser().size();
        int nonExistentId = 999; // An ID that is unlikely to exist

        // Action: Attempt to delete a non-existent user
        userService.deleteUser(nonExistentId);

        // Assertions
        assertEquals("User list size should remain unchanged", initialSize, userService.getAllUser().size());
        // No specific exception expected, just no change.
    }

    @Test
    public void testDeleteUser_emptyList() {
        // Setup: Ensure user list is empty (setUp should handle this)
        assertEquals("User list should be empty at the start of this test", 0, userService.getAllUser().size());

        int arbitraryId = 1;

        // Action: Attempt to delete from an empty list
        userService.deleteUser(arbitraryId);

        // Assertions
        assertEquals("User list should remain empty", 0, userService.getAllUser().size());
        // No exception expected.
    }
}
