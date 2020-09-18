package tw.com.businessmeet;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;

public class AddIntroductionActivity extends AppCompatActivity {
    private  String TAG = "MainActivity";
    //    private RecyclerView recyclerViewThrmatic;

    private TextView userId,password,userName,gender,mail,profession,tel;
    private ImageView avatar;
    private Button confirm;
    private BlueToothHelper blueTooth;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private AvatarHelper avatarHelper ;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl() ;
//    private List<DeviceItem> deviceItems ;
//    private ThematicListAdapter thematicListAdapter;
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(UserInformationBean... userInformationBeans) {
                    return userInformationService.add(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {
                    userInformationDAO.add(userInformationBean);
                    changeToAnotherPage(LoginActivity.class);
                }

                @Override
                public void onFail(int status,String message) {

                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_add);
        confirm = (Button)findViewById(R.id.confirm_introduction);
        userId = (TextView)findViewById(R.id.add_profile_user_id);
        password = (TextView)findViewById(R.id.add_profile_password);
        userName = (TextView) findViewById(R.id.add_profile_name);
        gender = (TextView) findViewById(R.id.add_profile_gender);
        profession = (TextView) findViewById(R.id.add_profile_profession);
        tel = (TextView) findViewById(R.id.add_profile_tel);
        mail = (TextView) findViewById(R.id.add_profile_mail);
        userId.setText("1");
        password.setText("1");
        userName.setText("1");
        gender.setText("1");
        profession.setText("1");
        tel.setText("1");;
        mail.setText("1");;
        avatar = (ImageView) findViewById(R.id.add_photo_button);
        avatarHelper = new AvatarHelper();
        openDB();
//        //啟動藍芽
        blueTooth = new BlueToothHelper(this);
        blueTooth.startBuleTooth();
        String myBlueTooth = blueTooth.getMyBuleTooth();
        Log.e("resultMy","======================");
        System.out.println("myBlueTooth"+myBlueTooth);
//        myBlueTooth = blueTooth.test();
        Log.e("resultMy","======================");
        System.out.println(myBlueTooth);
        String result = userInformationDAO.getId(myBlueTooth);

        if( result !=null && !result.equals("") ) {
            changeToAnotherPage(SelfIntroductionActivity.class);
        }
        confirm.setOnClickListener(confirmClick);
        avatar.setOnClickListener(choseAvatar);


    }
    private Button.OnClickListener choseAvatar = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, RequestCode.REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode !=RESULT_CANCELED) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.d("resultUri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    avatar.setImageBitmap(avatarHelper.toCircle(bitmap));
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }
    public View.OnClickListener confirmClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();


            ufb.setUserId(userId.getText().toString());
            ufb.setPassword(password.getText().toString());
            ufb.setName(userName.getText().toString());
            ufb.setGender(gender.getText().toString());
            ufb.setMail(mail.getText().toString());
            ufb.setProfession(profession.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(avatarHelper.setImageResource(avatar));
            ufb.setBluetooth(blueTooth.getMyBuleTooth());
            ufb.setRoleNo(3);
            if(checkData(ufb)) {
                AsyncTasKHelper.execute(addResponseListener, ufb);
            }
        }
    };

    public void changeToAnotherPage(Class classname){
        Intent intent = new Intent();
        intent.setClass(AddIntroductionActivity.this,classname);
        startActivity(intent);
    }
    private boolean checkData(UserInformationBean userInformationBean){
        String userIdStr = userInformationBean.getUserId();
        String passwordStr = userInformationBean.getPassword();
        String nameStr = userInformationBean.getName();
        String genderStr = userInformationBean.getGender();
        String mailStr = userInformationBean.getMail();
        String professionStr = userInformationBean.getProfession();
        String avatarStr = userInformationBean.getAvatar();
        String telStr = userInformationBean.getTel();
        if(userIdStr ==null || userIdStr.equals("")){
            Toast.makeText(this,"請輸入帳號",Toast.LENGTH_LONG).show();
        }else if(passwordStr == null || passwordStr.equals("")){
            Toast.makeText(this,"請輸入密碼",Toast.LENGTH_LONG).show();
        }else if(nameStr == null || nameStr.equals("")){
            Toast.makeText(this,"請輸入姓名",Toast.LENGTH_LONG).show();
        }else if(genderStr == null || genderStr.equals("")){
            Toast.makeText(this,"請輸入姓名",Toast.LENGTH_LONG).show();
        }else if(mailStr == null || mailStr.equals("")){
            Toast.makeText(this,"請輸入信箱",Toast.LENGTH_LONG).show();
        }else if(professionStr == null || professionStr.equals("")){
            Toast.makeText(this,"請輸入職業",Toast.LENGTH_LONG).show();
        }else if(telStr == null || telStr.equals("")){
            Toast.makeText(this,"請輸入電話",Toast.LENGTH_LONG).show();
        }else if(avatarStr == null || avatarStr.equals("")) {
            Toast.makeText(this,"請上傳圖片",Toast.LENGTH_LONG).show();
        }else{
            return true;
        }
        return false;
    }

}
