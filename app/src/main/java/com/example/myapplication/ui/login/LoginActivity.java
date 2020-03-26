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

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

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
        onclick();
        CheckLogin();
    }
//11111111111
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
                    ApiClient.getService().loginnew(user,pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<User>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }
                                @Override
                                public void onSuccess(User us) {
                                    Log.d("nnn", "onSuccess: "+us.getMessage());
                                    if (us.getMessage() == null){
                                        info info = us.getUser();
                                        if (us != null && info.getId()!= null){
                                            SessionManager.getInstance().setKeySaveToken(us.getAccessToken());
                                            SessionManager.getInstance().setKeySaveName(info.getName());
                                            SessionManager.getInstance().setKeyLogin(true);
                                            SessionManager.getInstance().setKeyRole(info.getRoleId());
//                                            String token = us.getAccessToken();
//                                            String id = info.getId();
//                                            String name = info.getName();
//                                            session.SetLogin(true,id,token,name);
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                            intent.putExtra("idne", id);
                                            startActivity(intent);

                                            ApiConfig config = ApiConfig.builder().context(LoginActivity.this).baseUrl(SessionManager.getInstance().getKeySaveCityName())
                                                    .auth(SessionManager.getInstance().getKeySaveToken())
                                                    .build();
                                            ApiClient.getInstance().init(config);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, " Message: "+ us.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.d("nnn", "onError: "+ e.getMessage());
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
                startActivity(intent);
            }
        });
    }
    private void CheckLogin(){
        if(!SessionManager.getInstance().Check()){//session.Check()
//            Toast.makeText(this, "Vui lòng đăng nhập!!!!!!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void Anhxa() {
        btnlogin = findViewById(R.id.btnLogin);
        tvforgot = findViewById(R.id.forgotPassTV);
        edtuser = findViewById(R.id.edt_userlogin);
        edtpass = findViewById(R.id.edt_passlogin);
    }
}
