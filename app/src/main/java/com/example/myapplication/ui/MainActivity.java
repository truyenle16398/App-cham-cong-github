package com.example.myapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.network.response.MessageResponse;
import com.example.myapplication.ui.model.sessionmanager;
import com.example.myapplication.retrofit.APIUtils;
import com.example.myapplication.retrofit.DataClient;
import com.example.myapplication.ui.login.LoginActivity;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DataClient mgetdata;
    sessionmanager session;
    SharedPreferences share;
    private AppBarConfiguration mAppBarConfiguration;
    public static String idd = "",tokenn = "",namee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgetdata = APIUtils.getdata();
        session = new sessionmanager(getApplicationContext());
        share = getSharedPreferences("android_demo",MODE_PRIVATE);
        idd = share.getString("idna","");
        tokenn = share.getString("tokenna","");
        namee = share.getString("namene","");
        Log.d("nnn", "onCreate: "+idd + "|||||"+ tokenn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_info, R.id.nav_attendance, R.id.nav_attendance_history,
                R.id.nav_setting, R.id.nav_vacation)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
//        String a = sharedPreferences.getString("user","null");
//        Toast.makeText(this, "khoa "+ a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_thoat:
                showAlert();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
//    private void showdialog() {
//        AlertDialog ad = new AlertDialog.Builder(getApplicationContext())
//                .create();
//        ad.setCancelable(false);
//        ad.setTitle("khoa");
//        ad.setMessage("nè");
//        ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        ad.show();
//    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void showAlert() {
        AlertDialog builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
//                .setTitle("Delete entry")
                .setMessage("Bạn có muốn thoát không?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
//                        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
//                        editor = sharedPreferences.edit();
//                        editor.clear();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
    private void logout(){
        ApiClient.getService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        String mess = messageResponse.getMessage();
                        if (!mess.isEmpty()){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // hủy trong stack
                            startActivity(intent);
                            session.SetLogin(false,null,null,null);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "loixxxxxxxx", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: logout "+ e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}

