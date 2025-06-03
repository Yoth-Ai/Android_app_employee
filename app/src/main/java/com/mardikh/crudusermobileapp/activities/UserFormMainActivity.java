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
        String email = etEmail.getText().toString();
        String gender = rbMale.isActivated() ? "Male" : "Female";
        if (username.isEmpty()){
            showToastMessage("Please enter username");
            etUsername.setFocusable(true);
            return;
        }
        if (email.isEmpty()){
            showToastMessage("Please enter email");
            etEmail.setFocusable(true);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("USERNAME", username);
        intent.putExtra("EMAIL", email);
        intent.putExtra("GENDER", gender);
        intent.putExtra("ROLE_ID", selectRole.getId());
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
        if (id==0){

        }
    }
}