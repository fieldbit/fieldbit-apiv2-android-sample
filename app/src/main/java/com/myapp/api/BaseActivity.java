package com.myapp.api;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

   private boolean debugMode = false;

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your main_menu into the menu
        getMenuInflater().inflate(R.menu.options, menu);
        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.id_options_debug:

                debugMode = !debugMode;
                if(!debugMode) {
                    item.setTitle(getString(R.string.menu_icon_debug_mode_on));
                }else{
                    item.setTitle(getString(R.string.menu_icon_debug_mode_off));

                }

            break;
            case R.id.id_about:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name)).setMessage(R.string.about_content)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
