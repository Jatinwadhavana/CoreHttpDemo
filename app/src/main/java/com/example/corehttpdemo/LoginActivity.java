package com.example.corehttpdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.corehttpdemo.employee.AddUpdateData;
import com.example.corehttpdemo.network.NetworkCall;
import com.example.corehttpdemo.utils.Constants;
import com.example.corehttpdemo.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private NetworkCall networkCall;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=new ProgressDialog(this);
        progressBar.setMessage(getString(R.string.msg_loading));
        setTitle(getString(R.string.login));
        if(SharedPref.getAppIsLogin(LoginActivity.this)){
            Intent intentHome=new Intent(LoginActivity.this, AddUpdateData.class);
            startActivity(intentHome);
            finish();
            return;
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnLogin=findViewById(R.id.btnLogin);
        EditText edtUserName=findViewById(R.id.edtUserName);
        EditText edtPass=findViewById(R.id.edtPass);
        networkCall=new NetworkCall();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUserName.getText().toString().trim().length()<=0){
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_username),Toast.LENGTH_LONG).show();
                }
                if(edtPass.getText().toString().trim().length()<=0){
                    Toast.makeText(LoginActivity.this, getString(R.string.msg_password),Toast.LENGTH_LONG).show();
                }
                progressBar.show();
                getLoginAPIObservable(edtUserName.getText().toString().trim(),edtPass.getText().toString().trim()).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(loginObserver);
            }
        });
    }

    private Observable<String> getLoginAPIObservable(String userName,String pass){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String response = networkCall.makeServiceCall(Constants.BASE_URL.concat(Constants.LOGIN_API),userName,pass);
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observer<String> loginObserver =new Observer<String>() {
        @Override
        public void onCompleted() {
            progressBar.hide();
        }

        @Override
        public void onError(Throwable e) {
            progressBar.hide();
            Toast.makeText(LoginActivity.this,"Error on response",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(String response) {
            Log.d("onNext",response);
            progressBar.hide();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if(jsonObject.getString("status").equalsIgnoreCase("ok")){
                    JSONObject jsonRes=jsonObject.getJSONObject("response");
                    JSONArray jsonArray=jsonRes.getJSONArray("table1");
                    if (jsonArray.length()>0){
                        String userDisplayName=jsonArray.getJSONObject(0).getString("userdisplayname");
                        SharedPref.setAppIsLogin(LoginActivity.this,true);
                        SharedPref.setAppUserDisplayName(LoginActivity.this,userDisplayName);
                        Intent intentHome=new Intent(LoginActivity.this, AddUpdateData.class);
                        startActivity(intentHome);
                        finishAffinity();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}