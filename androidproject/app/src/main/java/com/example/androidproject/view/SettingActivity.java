package com.example.androidproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;

import com.example.androidproject.App;
import com.example.androidproject.R;
import com.example.androidproject.dataBase.Note;
import com.example.androidproject.helper.MyToasty;
import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingActivity extends AppCompatActivity {
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        email = findViewById(R.id.SettingActivity_email);
        password = findViewById(R.id.SettingActivity_password);


    }
    public void sumbitEmailChange(View view)
    {
        final List<String> item = new ArrayList<>();
        final String sEmail = email.getText().toString();
        final String sPassword = password.getText().toString();

        if (!sEmail.isEmpty())
            item.add(sEmail);
        if (!sPassword.isEmpty())
            item.add(sPassword);

        if (item.size() == 0)
        {
            MyToasty myToasty = new MyToasty(this);
            myToasty.error("you didn't input any thing", true);
            return;
        }

        String massage = "";
        if (item.size() == 2)
        {
            massage = "email and password";
        }else if(!sEmail.isEmpty()){
            massage = "email";
        }else
        {
            massage = "password";
        }

        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("do you want to change "+massage+" ?")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                             @Override
                                             public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                 SharedPreferences.Editor editor =  getSharedPreferences(App.SHAREDPREFERENCES, MODE_PRIVATE).edit();
                                                 if (!sEmail.isEmpty())
                                                 {
                                                     editor.putString(App.SHAREDPREFERENCES_EMAIL, sEmail);
                                                 }
                                                 if (!sPassword.isEmpty())
                                                 {
                                                     editor.putString(App.SHAREDPREFERENCES_PASSWORD, sPassword);
                                                 }

                                                 editor.apply();



                                                 cleanAddItem();
                                                 sweetAlertDialog.cancel();
                                             }
                                         }


                ).show();


    }
    public void finishBackToHome(View view)
    {
        finish();
    }
    private void cleanAddItem()
    {
        email.setText("");
        password.setText("");
    }
}