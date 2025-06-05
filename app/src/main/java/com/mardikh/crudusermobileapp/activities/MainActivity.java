package com.mardikh.crudusermobileapp.activities;

import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mardikh.crudusermobileapp.R;
import com.mardikh.crudusermobileapp.adapters.CustomUserAdapter;
import com.mardikh.crudusermobileapp.models.Role;
import com.mardikh.crudusermobileapp.models.User;
import com.mardikh.crudusermobileapp.services.UserService;
import com.mardikh.crudusermobileapp.services.impl.UserServiceImpl;

public class MainActivity extends BaseActivity {
    public final static int RESULT_CODE_DATA = 2000;
    private UserService userService;
    private ListView listViewUser;
    private TextView tvAddUser;
    private CustomUserAdapter customUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvAddUser = findViewById(R.id.tvAddUser);
        userService = new UserServiceImpl();
        listViewUser = findViewById(R.id.lvUser);
        getUserData();
        tvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserFormMainActivity.class);
                startActivityForResult(intent, RESULT_CODE_DATA);
            }
        });
    }
    private void getUserData(){
        userService.getAllRoles();
        customUserAdapter = new CustomUserAdapter(this, userService.getAllUser(), new CustomUserAdapter.OnClickListener() {
            @Override
            public void onEdit(View view, User user) {
                Intent intent = new Intent(MainActivity.this, UserFormMainActivity.class);
                intent.putExtra("ID", user.getId());
                intent.putExtra("USERNAME", user.getName());
                intent.putExtra("EMAIL", user.getEmail());
                intent.putExtra("GENDER", user.getGender());
                intent.putExtra("ROLE_ID", user.getRole().getId());
                startActivityForResult(intent, RESULT_CODE_DATA);
            }

            @Override
            public void onDelete(User user) {
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete user: " + user.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            userService.deleteUser(user.getId());
                            getUserData(); // Refresh the list
                            showToastMessage("User " + user.getName() + " deleted.");
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
        listViewUser.setAdapter(customUserAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE_DATA && resultCode == RESULT_OK && data != null){
            String actionType = data.getStringExtra("ACTION_TYPE");

            if ("CREATE".equals(actionType)) {
                // User creation is already handled by UserFormMainActivity's call to userService.insertUser()
                // We just need to refresh the list and show a toast.
                // The original code here re-inserted the user, which might lead to duplicates if UserFormMainActivity also inserted.
                // String username = data.getStringExtra("USERNAME"); // For toast
                showToastMessage("User created successfully.");
            } else if ("UPDATE".equals(actionType)) {
                // User update is already handled by UserFormMainActivity's call to userService.updateUser()
                // We just need to refresh the list and show a toast.
                // int userId = data.getIntExtra("USER_ID", -1); // For more specific toast if needed
                showToastMessage("User updated successfully.");
            }
            // For both CREATE and UPDATE, or any other action that modifies data
            getUserData(); // Refresh the list
        }
    }
}