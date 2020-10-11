package tw.com.businessmeet;

import android.content.Intent;
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
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
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
    private FriendBean friendBean = new FriendBean();
    private Button confirmButton;
    private FriendDAO friendDAO;
    private DBHelper dh = null;
    private FriendServiceImpl friendServiceImpl = new FriendServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> addRemarkResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean>() {

        @Override
        public Call<ResponseBody<FriendBean>> request(FriendBean... friendBean) {
            return friendServiceImpl.update(friendBean[0]);
        }

        @Override
        public void onSuccess(FriendBean friendBean) {
            openDB();
            friendDAO.update(friendBean);
        }

        @Override
        public void onFail(int status, String message) {
        }
    };

    private void openDB() {
        dh = new DBHelper(getContext());
        friendDAO = new FriendDAO(dh);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        addProfileContent = (EditText) view.findViewById(R.id.addProfileContent_input);
        confirmButton = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(addProfileContent.getText().toString());
                friendBean.setRemark(addProfileContent.getText().toString());
                AsyncTasKHelper.execute(addRemarkResponseListener, friendBean);

            }
        });

        return view;
    }
}
