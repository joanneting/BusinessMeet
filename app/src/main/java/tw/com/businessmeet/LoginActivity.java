package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.network.ApplicationContext;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class LoginActivity extends AppCompatActivity {
    private UserInformationDAO userInformationDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        DBHelper dbHelper = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(dbHelper);

        System.out.println("App.get() : " + ApplicationContext.get());
        BluetoothHelper.startBluetooth(this);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        loginButton.setEnabled(true);
        String userId = DeviceHelper.getUserId(this, userInformationDAO);
        if (userId == null || userId.isEmpty()) {
            registerButton.setEnabled(true);
        }

        loginButton.setOnClickListener(view -> {
            UserInformationBean userInformationBean = new UserInformationBean();
            userInformationBean.setUserId(usernameEditText.getText().toString());
            userInformationBean.setPassword(passwordEditText.getText().toString());
            userInformationBean.setIdentifier(DeviceHelper.getIdentifier(this));
            AsyncTaskHelper.execute(() -> UserInformationServiceImpl.login(userInformationBean), loginBean -> {
                Intent it = new Intent(LoginActivity.this, NotificationService.class);
//            stopService(it);
                startService(it);
                System.out.println("loginBean.getUserInformationBean() = " + loginBean.getUserInformationBean());
                if (userId == null || userId.isEmpty()) {
                    userInformationDAO.add(loginBean.getUserInformationBean());
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SelfIntroductionActivity.class);
                startActivity(intent);
                finish();
                System.out.println("identty : " + loginBean.getIdentity());
            }, (status, message) -> {
                if (status == 401) {
                    Toast.makeText(getApplicationContext(), "帳號密碼錯誤", Toast.LENGTH_LONG).show();
                }
                Log.d("intomatched", "success");
            });
        });
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, AddIntroductionActivity.class);
            startActivity(intent);
        });
    }
}