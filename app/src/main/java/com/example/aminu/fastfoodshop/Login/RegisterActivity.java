package com.example.aminu.fastfoodshop.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.aminu.fastfoodshop.HomeActivity.HomeActivity;
import com.example.aminu.fastfoodshop.R;

/**
 * Created by aminu on 9/16/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: starting RegisterActivity.");
        mContext =RegisterActivity.this;

        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: RegisterActivity to HomeActivity.");
                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
