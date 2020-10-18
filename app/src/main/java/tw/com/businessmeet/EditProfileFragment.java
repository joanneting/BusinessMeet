package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class EditProfileFragment extends Fragment {
    private EditText addProfileContent;
    private final FriendBean fb = new FriendBean();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        addProfileContent = view.findViewById(R.id.addProfileContent_input);
        String remark = getActivity().getIntent().getStringExtra("remark");
        if (remark != null && !remark.isEmpty()) {
            addProfileContent.append(remark);
        }
        Button confirmButton = view.findViewById(R.id.addColumn_dialog_confirmButton);
        confirmButton.setOnClickListener(v -> {
//                openDB();
//                friendDAO.update(fb);
            fb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
            fb.setMatchmakerId(getActivity().getIntent().getStringExtra("matchmakerId"));
            fb.setFriendId(getActivity().getIntent().getStringExtra("friendId"));
            fb.setRemark(addProfileContent.getText().toString());
            AsyncTaskHelper.execute(() -> FriendServiceImpl.update(fb), friendBean -> changeToSelfIntroductionPage());
        });
        return view;
    }

    private void changeToSelfIntroductionPage() {
        Intent intent = new Intent(getActivity(), FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        bundle.putInt("friendNo", getActivity().getIntent().getIntExtra("friendNo", 0));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
