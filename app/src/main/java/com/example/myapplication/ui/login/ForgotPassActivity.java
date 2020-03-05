package com.example.myapplication.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class ForgotPassActivity extends AppCompatActivity {

   TextView tvback;
    private AppCompatButton btn_reset;
    private EditText et_email,et_code,et_password;
    private TextView tv_timer;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        anhxa();
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_email.setVisibility(View.GONE);
                et_code.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.VISIBLE);
                tv_timer.setVisibility(View.VISIBLE);
                btn_reset.setText("ĐỔI MẬT KHẨU");
                startCountdownTimer();
            }
        });
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(ForgotPassActivity.this, "Time Out ! Request again to reset password.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    @SuppressLint("WrongViewCast")
    private void anhxa() {
        tvback = findViewById(R.id.textViewBack);

        btn_reset = findViewById(R.id.btn_reset);
        tv_timer = findViewById(R.id.timer);
        et_code = findViewById(R.id.et_code);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        tv_timer.setVisibility(View.GONE);
    }
}
