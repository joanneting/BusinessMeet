package tw.com.businessmeet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private EditText addProfileContent;
    private String friendNo, remark, matchMakerId, friendId;
    private Button confirmButton;
    private FriendBean fb = new FriendBean();
    private FriendDAO friendDAO;
    private DBHelper dbHelper;
    private FriendServiceImpl friendServiceImpl = new FriendServiceImpl();

    private AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> addRemarkResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean>() {

        @Override
        public Call<ResponseBody<FriendBean>> request(FriendBean... friendBeans) {
            return friendServiceImpl.update(friendBeans[0]);
        }

        @Override
        public void onSuccess(FriendBean friendBean) {
            changeToSelfIntroductionPage();
        }

        @Override
        public void onFail(int status, String message) {
        }
    };

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_editProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        addProfileContent = (EditText) view.findViewById(R.id.addProfileContent_input);
        remark = getActivity().getIntent().getStringExtra("remark");
        if (remark != null && remark != "") {
            addProfileContent.append(remark);
        }
        confirmButton = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDB();
//                friendDAO.update(fb);
                fb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                fb.setMatchmakerId(getActivity().getIntent().getStringExtra("matchmakerId"));
                fb.setFriendId(getActivity().getIntent().getStringExtra("friendId"));
                fb.setRemark(addProfileContent.getText().toString());
                AsyncTasKHelper.execute(addRemarkResponseListener, fb);
            }
        });
        return view;
    }

//    private void openDB() {
//        dbHelper = new DBHelper(getContext());
//        friendDAO = new FriendDAO(dbHelper);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        fb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
//        System.out.println("fb.getFriendNo() = " + fb.getFriendNo());
//        fb.setMatchmakerId(getActivity().getIntent().getStringExtra("matchmakerId"));
//        System.out.println("fb.getMatchmakerId() = " + fb.getMatchmakerId());
//        fb.setFriendId(getActivity().getIntent().getStringExtra("friendId"));
//        System.out.println("fb.getFriendId() = " + fb.getFriendId());
//        fb.setRemark(addProfileContent.getText().toString());
//        System.out.println("fb.getRemark() = " + fb.getRemark());
//        Cursor cursor = friendDAO.search(fb);
//        if (cursor.moveToFirst()) {
//            do {
//                friendNo = cursor.getString(cursor.getColumnIndex("friend_no"));
//                matchMakerId = cursor.getString(cursor.getColumnIndex("matchmaker_id"));
//                friendId = cursor.getString(cursor.getColumnIndex("friend_id"));
//                remark = cursor.getString(cursor.getColumnIndex("remark"));
//            } while (cursor.moveToNext());
//        }
//    }

//    public void searchUserInformation() {
//        SQLiteDatabase db = DH.getWritableDatabase();
//        UserInformationBean ufb = new UserInformationBean();
//        blueToothHelper = new BlueToothHelper(this);
//        blueToothHelper.startBuleTooth();
//        ufb.setUserId(blueToothHelper.getUserId());
//        Cursor cursor = userInformationDAO.searchAll(ufb);
//
//        if (cursor.moveToFirst()) {
//            do {
//                name = cursor.getString(cursor.getColumnIndex("name"));
//                Log.d("edit", name);
//                pro = cursor.getString(cursor.getColumnIndex("profession"));
//                gen = cursor.getString(cursor.getColumnIndex("gender"));
//                mail = cursor.getString(cursor.getColumnIndex("mail"));
//                phone = cursor.getString(cursor.getColumnIndex("tel"));
//                avatar.setImageBitmap(avatarHelper.getImageResource(cursor.getString(cursor.getColumnIndex("avatar"))));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

    private void changeToSelfIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        bundle.putInt("friendNo", getActivity().getIntent().getIntExtra("friendNo", 0));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
