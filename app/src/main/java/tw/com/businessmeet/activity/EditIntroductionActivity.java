package tw.com.businessmeet.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BluetoothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class EditIntroductionActivity extends AppCompatActivity {
    private TextView userName, profession, gender, email, tel;
    private String name, pro, gen, mail, phone;
    private ImageView avatar;
    private ImageButton editConfirmButtom;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private DBHelper DH;
    private UserInformationDAO userInformationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_edit);
        editConfirmButtom = (ImageButton) findViewById(R.id.editProfileConfirmButtom);
        editConfirmButtom.setOnClickListener(editConfirmClick);
        avatar = (ImageView) findViewById(R.id.profilePhoto);
        avatar.setOnClickListener(choseAvatar);
        userName = (TextView) findViewById(R.id.profileName);
        profession = (TextView) findViewById(R.id.profileProfession);
        gender = (TextView) findViewById(R.id.profileGender);
        tel = (TextView) findViewById(R.id.profileTel);
        email = (TextView) findViewById(R.id.profileMail);

        openDB();
        searchUserInformation();
        userName.append(name);
        profession.append(pro);
        gender.append(gen);
        email.append(mail);
        tel.append(phone);
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation() {
        UserInformationBean ufb = new UserInformationBean();
        BluetoothHelper.startBluetooth(this);
        ufb.setUserId(DeviceHelper.getUserId(this, userInformationDAO));
        Cursor cursor = userInformationDAO.searchAll(ufb);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
                pro = cursor.getString(cursor.getColumnIndex("profession"));
                gen = cursor.getString(cursor.getColumnIndex("gender"));
                mail = cursor.getString(cursor.getColumnIndex("mail"));
                phone = cursor.getString(cursor.getColumnIndex("tel"));
                avatar.setImageBitmap(AvatarHelper.getImageResource(cursor.getString(cursor.getColumnIndex("avatar"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setUserId(DeviceHelper.getUserId(EditIntroductionActivity.this, userInformationDAO));
            ufb.setIdentifier(DeviceHelper.getIdentifier(EditIntroductionActivity.this));
            ufb.setName(userName.getText().toString());
            ufb.setProfession(profession.getText().toString());
            ufb.setGender(gender.getText().toString());
            ufb.setMail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(AvatarHelper.setImageResource(avatar));
            userInformationDAO.update(ufb);
            AsyncTaskHelper.execute(() -> UserInformationServiceImpl.update(ufb), userInformationDAO::update);
            changeToSelfIntroductionPage();
            finish();
        }
    };

    public void changeToSelfIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(EditIntroductionActivity.this, SelfInformationActivity.class);
        startActivity(intent);
    }

    private Button.OnClickListener choseAvatar = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                avatar.setImageBitmap(AvatarHelper.toCircle(bitmap));
            } catch (Exception e) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}