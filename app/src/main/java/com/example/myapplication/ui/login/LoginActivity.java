package com.example.myapplication.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.SessionManager;
import com.example.myapplication.ui.model.ApiConfig;
import com.example.myapplication.ui.model.User;
import com.example.myapplication.ui.model.info;
import com.example.myapplication.ui.model.sessionmanager;
import com.example.myapplication.retrofit.APIUtils;
import com.example.myapplication.retrofit.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    DataClient mgetdata;
    Button btnlogin;
    TextView tvforgot;
    EditText edtuser,edtpass;
    String user;
    String pass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String USER_KEY = "user";
    String PASS_KEY = "pass";
    sessionmanager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_login) ;
        session = new sessionmanager(getApplication());
//        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
//        edtuser.setText(sharedPreferences.getString(USER_KEY,""));
//        edtpass.setText(sharedPreferences.getString(PASS_KEY,""));
        mgetdata = APIUtils.getdata();
        onclick();
        CheckLogin();
    }

    private void onclick() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = edtuser.getText().toString();
                pass = edtpass.getText().toString();
                if (user.isEmpty() || user.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ!!", Toast.LENGTH_SHORT).show();
                } else if (user.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản!!", Toast.LENGTH_SHORT).show();
                } else if (pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu!!", Toast.LENGTH_SHORT).show();
                } else {
                    Call<User> callback = mgetdata.login(user,pass);
                    callback.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.d("nnn", "onResponse: "+response.toString());
                            User us = response.body();
                            SessionManager.getInstance().setKeySaveToken(response.body().getAccessToken());
                            if (us.getMessage() == null){
                                info info = us.getUser();
                                if (us != null && info.getId()!= null){
                                    String token = us.getAccessToken();
                                    String id = info.getId();
                                    String name = info.getName();
                                    session.SetLogin(true,id,token,name);
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("idne", id);
                                    startActivity(intent);

                                    ApiConfig config = ApiConfig.builder().context(LoginActivity.this).baseUrl(SessionManager.getInstance().getKeySaveCityName())
                                            .auth(SessionManager.getInstance().getKeySaveToken())
                                            .build();
                                    ApiClient.getInstance().init(config);

                                    finish();
//                                intent.putExtra("emailne",info.getEmail());
//                                dosave();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                            Toast.makeText(LoginActivity.this, " "+ us.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("nnn", "onFailure: "+ t.getMessage());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                edtuser.onEditorAction(EditorInfo.IME_ACTION_DONE);
                edtpass.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void CheckLogin(){
        if(!session.Check()){
//            Toast.makeText(this, "Vui lòng đăng nhập!!!!!!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void dosave() {
        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("user",edtuser.getText().toString().trim());
        editor.putString("pass",edtpass.getText().toString().trim());
        editor.commit();
    }


    private void Anhxa() {
        btnlogin = findViewById(R.id.btnLogin);
        tvforgot = findViewById(R.id.forgotPassTV);
        edtuser = findViewById(R.id.edt_userlogin);
        edtpass = findViewById(R.id.edt_passlogin);
    }
}
