package com.mardikh.crudusermobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mardikh.crudusermobileapp.R;
import com.mardikh.crudusermobileapp.adapters.CustomRoleAdapter;
import com.mardikh.crudusermobileapp.models.Role;
import com.mardikh.crudusermobileapp.services.UserService;
import com.mardikh.crudusermobileapp.services.impl.UserServiceImpl;

public class UserFormMainActivity extends BaseActivity {
    private UserService userService;
    private Spinner spinnerRole;
    private Role selectRole;
    private CustomRoleAdapter customRoleAdapter;
    private EditText etUsername, etEmail;
    private RadioButton rbMale, rbFemale;
    private Button btnCreate, btnBack;
    private boolean isEditMode = false;
    private int currentUserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_form_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        userService = new UserServiceImpl();
        customRoleAdapter = new CustomRoleAdapter(this, userService.getAllRoles());
        spinnerRole.setAdapter(customRoleAdapter);
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectRole = userService.getAllRoles().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateUser();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void onCreateUser(){
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim(); // Also trim email
        String gender = rbMale.isChecked() ? "Male" : "Female"; // Use isChecked() for RadioButton

        if (username.isEmpty()){
            showToastMessage("Please enter username");
            etUsername.requestFocus(); // Use requestFocus()
            return;
        }
        if (email.isEmpty()){
            showToastMessage("Please enter email");
            etEmail.requestFocus(); // Use requestFocus()
            return;
        }
        // Ensure a role is selected
        if (selectRole == null) {
            showToastMessage("Please select a role");
            // Potentially open the spinner or highlight it
            return;
        }

        com.mardikh.crudusermobileapp.models.User user = new com.mardikh.crudusermobileapp.models.User();
        user.setName(username);
        user.setEmail(email);
        user.setGender(gender);
        user.setRole(selectRole);

        Intent intent = new Intent();
        if (isEditMode) {
            user.setId(currentUserId);
            userService.updateUser(user);
            intent.putExtra("USER_ID", currentUserId); // Pass back the ID of the updated user
            intent.putExtra("ACTION_TYPE", "UPDATE");
        } else {
            userService.insertUser(user);
            // For a new user, ID is set by UserServiceImpl, so we don't set it here.
            // The existing extras are fine for indicating what was added.
            intent.putExtra("USERNAME", username);
            intent.putExtra("EMAIL", email);
            intent.putExtra("GENDER", gender);
            intent.putExtra("ROLE_ID", selectRole.getId());
            intent.putExtra("ACTION_TYPE", "CREATE");
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    private void initView(){
        spinnerRole = findViewById(R.id.spinnerRole);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale = findViewById(R.id.rbMale);
        btnBack = findViewById(R.id.btnBack);
        btnCreate = findViewById(R.id.btnCreate);
        rbMale.setChecked(true);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        if (id != 0){
            // Ensure userService is initialized before calling its methods
            if (userService == null) {
                userService = new UserServiceImpl();
            }
            // Ensure roles are loaded for spinner and for finding role by ID
            if (userService.getAllRoles().isEmpty()) {
                // This typically shouldn't happen if getAllRoles is called in onCreate
                // but as a safeguard:
                customRoleAdapter = new CustomRoleAdapter(this, userService.getAllRoles());
                spinnerRole.setAdapter(customRoleAdapter);
            }

            com.mardikh.crudusermobileapp.models.User user = userService.getUserById(id);
            if (user != null) {
                isEditMode = true;
                currentUserId = user.getId(); // Or currentUserId = id;
                etUsername.setText(user.getName());
                etEmail.setText(user.getEmail());

                if (user.getGender().equalsIgnoreCase("Male")) {
                    rbMale.setChecked(true);
                } else if (user.getGender().equalsIgnoreCase("Female")) {
                    rbFemale.setChecked(true);
                }

                // Set spinner selection
                if (user.getRole() != null) {
                    int rolePosition = -1;
                    for (int i = 0; i < userService.getAllRoles().size(); i++) {
                        if (userService.getAllRoles().get(i).getId() == user.getRole().getId()) {
                            rolePosition = i;
                            break;
                        }
                    }
                    if (rolePosition != -1) {
                        spinnerRole.setSelection(rolePosition);
                    }
                }
                btnCreate.setText("Update");
            }
        }
    }
}