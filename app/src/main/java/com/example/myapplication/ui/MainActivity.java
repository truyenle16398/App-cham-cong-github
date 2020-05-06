package com.example.myapplication.ui;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.network.response.MessageResponse;
import com.example.myapplication.ui.fragment.ApprovalSabbaticalFragment;
import com.example.myapplication.ui.fragment.CreateVacationFragment;
import com.example.myapplication.ui.model.sessionmanager;
import com.example.myapplication.ui.login.LoginActivity;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    sessionmanager session;
    SharedPreferences share;
    private AppBarConfiguration mAppBarConfiguration;
    public static String idd = "",tokenn = "",namee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mgetdata = APIUtils.getdata();
//        session = new sessionmanager(getApplicationContext());
//        share = getSharedPreferences("android_demo",MODE_PRIVATE);
        namee = SessionManager.getInstance().getKeySaveName();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_info,
                R.id.nav_attendance,
                R.id.nav_attendance_history,
                R.id.nav_list_register_for_leave,
                R.id.nav_list_report,
                R.id.nav_list_report_date,
                R.id.nav_approval_sabbatical,
                R.id.nav_vacation,
                R.id.nav_createvacation)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        String role = SessionManager.getInstance().getKeyRole();
        if (role.equals("1")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_approval_sabbatical).setVisible(false);
        }

//        MenuItem checkitem = navigationView.getMenu().findItem(R.id.nav_approval_sabbatical);
//        checkitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//
//                DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//
//                return true;
//            }
//        });

        ///------------------- fire base
    }

    private void gettoken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
//                        Toast.makeText(MainActivity.this, ""+ token, Toast.LENGTH_SHORT).show();
                        Log.d("nnn", "token: "+ token);
                    }
                });
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
                        if (messageResponse.getMessage() != null){
                            Log.d("nnn", "logout del token: "+ SessionManager.getInstance().getKeySaveToken());
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // hủy trong stack
                            startActivity(intent);
                            SessionManager.getInstance().setKeySaveToken("");
                            SessionManager.getInstance().setKeySaveCheck(true);
                            SessionManager.getInstance().setKeyLogin(false);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_drawer, menu);
//        MenuItem   item = menu.findItem(R.id.admin);
//        item.setVisible(true);
//        invalidateOptionsMenu();
//        return true;//super.onPrepareOptionsMenu(menu)
//    }
}

