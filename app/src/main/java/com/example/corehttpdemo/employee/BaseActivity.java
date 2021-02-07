package com.example.corehttpdemo.employee;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.corehttpdemo.employee.database.BaseDB;

public class BaseActivity extends AppCompatActivity {
    protected BaseDB baseDB;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseDB=new BaseDB(this);
    }
}
