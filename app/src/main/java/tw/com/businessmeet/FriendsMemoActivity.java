package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;


public class FriendsMemoActivity extends AppCompatActivity {
    private TextView memo;
    private String friendId;
    private ImageButton editProfileConfirmButtom;
    private DBHelper DH;
    private FriendDAO friendDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_edit_introduction);

        friendId = getIntent().getStringExtra("friendId");
        memo = findViewById(R.id.friends_memo);
        editProfileConfirmButtom = findViewById(R.id.editProfileConfirmButtom);
        editProfileConfirmButtom.setOnClickListener(editConfirmClick);
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(DeviceHelper.getUserId(this));
        friendBean.setFriendId(friendId);
        AsyncTaskHelper.execute(() -> FriendServiceImpl.search(friendBean), friendBeans -> {
            if (friendBeans.get(0).getRemark() != null) {
                memo.append(friendBeans.get(0).getRemark());
            }
        });
        openDB();
    }

    private void openDB() {
        DH = new DBHelper(this);
        friendDAO = new FriendDAO(DH);
    }

    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FriendBean friendBean = new FriendBean();
            friendBean.setMatchmakerId(DeviceHelper.getUserId(FriendsMemoActivity.this));
            friendBean.setFriendId(friendId);
            friendBean.setRemark(memo.getText().toString());
            AsyncTaskHelper.execute(() -> FriendServiceImpl.update(friendBean), updateFriendBean -> {
                friendDAO.update(updateFriendBean);
                Intent intent = new Intent();
                intent.setClass(FriendsMemoActivity.this, FriendsIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("friendId", updateFriendBean.getFriendId());
                intent.putExtras(bundle);
                startActivity(intent);
            });
            friendDAO.update(friendBean);
        }
    };
}