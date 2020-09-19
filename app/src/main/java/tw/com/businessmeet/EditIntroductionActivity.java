package tw.com.businessmeet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.AvatarHelper;

public class EditIntroductionActivity extends AppCompatActivity {

    private TextView userName, profession, gender, email, tel;
    private String name, pro, gen, mail, phone;
    private ImageView avatar;
    private ImageButton editConfirmButtom;
    private AvatarHelper avatarHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private BlueToothHelper blueToothHelper;
    private DBHelper DH;
    private UserInformationDAO userInformationDAO;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean> updateResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(UserInformationBean... userInformationBeans) {
                    return userInformationService.update(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {

                }

                @Override
                public void onFail(int status,String message) {

                }
            };
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
        avatarHelper = new AvatarHelper();

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
        SQLiteDatabase db = DH.getWritableDatabase();
        UserInformationBean ufb = new UserInformationBean();
        blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setUserId(blueToothHelper.getUserId());
        Cursor cursor = userInformationDAO.searchAll(ufb);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("edit", name);
                pro = cursor.getString(cursor.getColumnIndex("profession"));
                gen = cursor.getString(cursor.getColumnIndex("gender"));
                mail = cursor.getString(cursor.getColumnIndex("mail"));
                phone = cursor.getString(cursor.getColumnIndex("tel"));
                avatar.setImageBitmap(avatarHelper.getImageResource(cursor.getString(cursor.getColumnIndex("avatar"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setUserId(blueToothHelper.getUserId());
            ufb.setBluetooth(blueToothHelper.getMyBuleTooth());
            ufb.setName(userName.getText().toString());
            ufb.setProfession(profession.getText().toString());
            ufb.setGender(gender.getText().toString());
            ufb.setMail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(avatarHelper.setImageResource(avatar));
            userInformationDAO.update(ufb);
            AsyncTasKHelper.execute(updateResponseListener,ufb);
            changeToSelfIntroductionPage();
        }
    };

    public void changeToSelfIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(EditIntroductionActivity.this, SelfIntroductionActivity.class);
        startActivity(intent);
    }

    private Button.OnClickListener choseAvatar = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("resultUri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try{
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                avatar.setImageBitmap(avatarHelper.toCircle(bitmap));
            }catch(Exception e){
                Log.d("Exception",e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}