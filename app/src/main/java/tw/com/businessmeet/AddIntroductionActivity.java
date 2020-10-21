package tw.com.businessmeet;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class AddIntroductionActivity extends AppCompatActivity {
    private TextView userId, password, userName, gender, mail, profession, tel;
    private ImageView avatar;
    private Button confirm;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_add);
        confirm = findViewById(R.id.confirm_introduction);
        userId = findViewById(R.id.add_profile_user_id);
        password = findViewById(R.id.add_profile_password);
        userName = findViewById(R.id.add_profile_name);
        gender = findViewById(R.id.add_profile_gender);
        profession = findViewById(R.id.add_profile_profession);
        tel = findViewById(R.id.add_profile_tel);
        mail = findViewById(R.id.add_profile_mail);
        userId.setText("1");
        password.setText("1");
        userName.setText("名字");
        gender.setText("女");
        profession.setText("職業");
        tel.setText("電話");
        mail.setText("信箱");
        avatar = findViewById(R.id.add_photo_button);
        openDB();
//        //啟動藍芽
        BluetoothHelper.startBluetooth(this);
        String identifier = DeviceHelper.getIdentifier(this);
        String result = userInformationDAO.getId(identifier);

        if (result != null && !result.equals("")) {
            changeToAnotherPage(SelfIntroductionActivity.class);
        }
        confirm.setOnClickListener(view -> {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setUserId(userId.getText().toString());
            ufb.setPassword(password.getText().toString());
            ufb.setName(userName.getText().toString());
            ufb.setGender(gender.getText().toString());
            ufb.setMail(mail.getText().toString());
            ufb.setProfession(profession.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(AvatarHelper.setImageResource(avatar));
            ufb.setIdentifier(DeviceHelper.getIdentifier(this));
            ufb.setRoleNo(3);
            if (checkData(ufb)) {
                AsyncTaskHelper.execute(() -> UserInformationServiceImpl.add(ufb), userInformationBean -> {
                    userInformationDAO.add(userInformationBean);
                    changeToAnotherPage(LoginActivity.class);
                });
            }
        });
        avatar.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, RequestCode.REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    avatar.setImageBitmap(AvatarHelper.toCircle(bitmap));
                } catch (Exception e) {
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void changeToAnotherPage(Class classname) {
        Intent intent = new Intent();
        intent.setClass(AddIntroductionActivity.this, classname);
        startActivity(intent);
    }

    private boolean checkData(UserInformationBean userInformationBean) {
        String userIdStr = userInformationBean.getUserId();
        String passwordStr = userInformationBean.getPassword();
        String nameStr = userInformationBean.getName();
        String genderStr = userInformationBean.getGender();
        String mailStr = userInformationBean.getMail();
        String professionStr = userInformationBean.getProfession();
        String avatarStr = userInformationBean.getAvatar();
        String telStr = userInformationBean.getTel();
        if (userIdStr == null || userIdStr.equals("")) {
            Toast.makeText(this, "請輸入帳號", Toast.LENGTH_LONG).show();
        } else if (passwordStr == null || passwordStr.equals("")) {
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_LONG).show();
        } else if (nameStr == null || nameStr.equals("")) {
            Toast.makeText(this, "請輸入姓名", Toast.LENGTH_LONG).show();
        } else if (genderStr == null || genderStr.equals("")) {
            Toast.makeText(this, "請輸入姓名", Toast.LENGTH_LONG).show();
        } else if (mailStr == null || mailStr.equals("")) {
            Toast.makeText(this, "請輸入信箱", Toast.LENGTH_LONG).show();
        } else if (professionStr == null || professionStr.equals("")) {
            Toast.makeText(this, "請輸入職業", Toast.LENGTH_LONG).show();
        } else if (telStr == null || telStr.equals("")) {
            Toast.makeText(this, "請輸入電話", Toast.LENGTH_LONG).show();
        } else if (avatarStr == null || avatarStr.equals("")) {
            Toast.makeText(this, "請上傳圖片", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

}
