package tw.com.businessmeet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;


import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.GroupsServiceImpl;

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
    private EditText addProfileContent, editGroupDialogInput;
    private String friendNo, remark, matchMakerId, friendId;
    private Button confirmButton, confirmDialogButton, cancelDialogButton;
    private Chip addGroupButton;
    private ChipGroup chipGroup;
    private FriendBean fb = new FriendBean();
    private GroupsBean gb = new GroupsBean();
    private FriendBean friendBean = new FriendBean();
    private GroupsBean groupsBean = new GroupsBean();
//    private FriendServiceImpl friendServiceImpl = new FriendServiceImpl();
//    private GroupsServiceImpl groupsServiceImpl = new GroupsServiceImpl();
//
//    private AsyncTaskHelper.OnResponseListener<GroupsBean, GroupsBean> addResponseListener = new AsyncTaskHelper.OnResponseListener<GroupsBean, GroupsBean>() {
//        @Override
//        public Call<ResponseBody<GroupsBean>> request(GroupsBean... groupsBeans) {
//            return groupsServiceImpl.add(groupsBeans[0]);
//        }
//
//        @Override
//        public void onSuccess(GroupsBean groupsBean) {
//        }
//
//        @Override
//        public void onFail(int status, String message) {
//        }
//    };
//
//    private AsyncTaskHelper.OnResponseListener<FriendBean, FriendBean> addRemarkResponseListener = new AsyncTaskHelper.OnResponseListener<FriendBean, FriendBean>() {
//
//        @Override
//        public Call<ResponseBody<FriendBean>> request(FriendBean... friendBeans) {
//            return friendServiceImpl.update(friendBeans[0]);
//        }
//
//        @Override
//        public void onSuccess(FriendBean friendBean) {
//
//            changeToSelfIntroductionPage();
//        }
//
//        @Override
//        public void onFail(int status, String message) {
//        }
//    };

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        chipGroup = (ChipGroup) view.findViewById(R.id.chooseGroup_chipGroup);
        addGroupButton = (Chip) view.findViewById(R.id.addGroupButton);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = inflater.inflate(R.layout.friend_edit_group, null);
                builder.setView(view);
                builder.create();
                AlertDialog alertDialog = builder.show();

                editGroupDialogInput = (EditText) view.findViewById(R.id.editGroup_dialog_Input);
                confirmDialogButton = (Button) view.findViewById(R.id.editGroup_dialog_confirmButton);
                confirmDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editGroupDialogInput.getText().toString() != null || !editGroupDialogInput.getText().toString().equals("")) {
                            gb.setName(editGroupDialogInput.getText().toString());
                            gb.setUserId(getActivity().getIntent().getStringExtra("userId"));
                            System.out.println("gb.getName() = " + gb.getName());
                            System.out.println("gb.getUserId() = " + gb.getUserId());
                            AsyncTaskHelper.execute(() -> GroupsServiceImpl.add(gb), groupsBean -> {
                            });
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                            LayoutInflater chipInflater = LayoutInflater.from(getActivity());
                            Chip chip = new Chip(getActivity());
                            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getActivity(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                            chip.setChipDrawable(chipDrawable);
                            chip.setText(gb.getName());
                            chipGroup.addView(chip);
                        } else {
                            Toast.makeText(getContext(), "未輸入群組名稱", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                cancelDialogButton = (Button) view.findViewById(R.id.editGroup_dialog_cancelButton);
                cancelDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        });
        addProfileContent = (EditText) view.findViewById(R.id.addProfileContent_input);
        remark = getActivity().getIntent().getStringExtra("remark");
        if (remark != null && remark != "") {
            addProfileContent.append(remark);
        }
        confirmButton = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                fb.setMatchmakerId(getActivity().getIntent().getStringExtra("matchmakerId"));
                fb.setFriendId(getActivity().getIntent().getStringExtra("friendId"));
                fb.setRemark(addProfileContent.getText().toString());
                AsyncTaskHelper.execute(() -> FriendServiceImpl.update(fb), friendBean -> {
                    changeToSelfIntroductionPage();
                });
            }
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
