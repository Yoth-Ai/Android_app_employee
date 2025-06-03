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
        });
        listViewUser.setAdapter(customUserAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        if(requestCode==RESULT_CODE_DATA && requestCode==RESULT_OK){
            User user = new User();
            user.setName(data.getStringExtra("USERNAME"));
            user.setEmail(data.getStringExtra("EMAIL"));
            user.setGender(data.getStringExtra("GENDER"));
            Role role= userService.getRoleById(data.getIntExtra("ROLE_ID", 0));
            user.setRole(role);
            userService.insertUser(user);
            getUserData();
            showToastMessage("Insert data success");
        }
    }
}