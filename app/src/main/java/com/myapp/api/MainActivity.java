package com.myapp.api;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.myapp.api.usecase.MainFlowActivity;

public class MainActivity extends BaseActivity {

    private Button btOpenAllApis;
    private Button btOpenUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();

        if(!isValidCredential()){
            closeTheApp();
        }
        btOpenAllApis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openAuthActivity();
            }
        });
        btOpenUseCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTicket();
            }
        });

    }

    private void closeTheApp() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setCancelable(false)
                .setTitle(getString(R.string.app_name)).setMessage(R.string.no_credential_message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private boolean isValidCredential() {
        return  !getString(R.string.api_id).isEmpty() && !getString(R.string.api_secret).isEmpty();
    }

    private void openTicket() {
        Intent intent = new Intent(this, MainFlowActivity.class);
        startActivity(intent);
    }

    private void openAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    private void initButtons() {
        btOpenAllApis = findViewById(R.id.bt_all_endpoints);
        btOpenUseCase = findViewById(R.id.bt_open_use_case);
    }
}
