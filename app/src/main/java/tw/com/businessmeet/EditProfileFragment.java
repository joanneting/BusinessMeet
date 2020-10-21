package tw.com.businessmeet;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
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


import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;
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
    private Chip addGroupButton, currentGroup;
    private ChipGroup chipGroup;
    private FriendBean fb = new FriendBean();
    private Integer friendGroupNo, currentGroupNo, updateGroupNo;


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
        currentGroup = (Chip) view.findViewById(R.id.currentGroupTitleChip);
        chipGroup = (ChipGroup) view.findViewById(R.id.chooseGroup_chipGroup);
        FriendGroupBean fgb = new FriendGroupBean();
        fgb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        GroupsBean gb = new GroupsBean();
        AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.search(fgb), friendGroupBeanList -> {
            if (friendGroupBeanList.size() > 0 || (friendGroupBeanList.size() == 1 && friendGroupBeanList.get(0).getCreateDate() != null)) {
                gb.setGroupNo(friendGroupBeanList.get(0).getGroupNo());
                friendGroupNo = friendGroupBeanList.get(0).getFriendGroupNo();
                System.out.println("friendGroupNo = " + friendGroupNo);
            }
            AsyncTaskHelper.execute(() -> GroupsServiceImpl.search(gb), groupsBeanList -> {
                String chipIndex;
                for (int i = 0; i < groupsBeanList.size(); i++) {
                    if (groupsBeanList.get(i).getUserId().equals(getActivity().getIntent().getStringExtra("userId"))) {
                        groupsBeanList.get(i).getGroupNo();
                        LayoutInflater chipInflater = LayoutInflater.from(getActivity());
                        Chip chip = new Chip(getActivity());
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getActivity(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                        chip.setChipDrawable(chipDrawable);
                        chip.setId(groupsBeanList.get(i).getGroupNo());
                        System.out.println("chipId = " + chip.getId());
                        chip.setText(groupsBeanList.get(i).getName());
                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), chip.getText().toString() + chip.getId(), Toast.LENGTH_SHORT).show();
                                currentGroup.setText(chip.getText().toString());
                                updateGroupNo = chip.getId();
                            }
                        });
                        chipGroup.addView(chip);

                    }
                }
                for (int i = 0; i < groupsBeanList.size(); i++) {
                    if (groupsBeanList.get(i).getGroupNo() == gb.getGroupNo()) {
                        System.out.println(groupsBeanList.get(i).getName());
                        currentGroup.setText(groupsBeanList.get(i).getName());
                        currentGroupNo = groupsBeanList.get(i).getGroupNo();
                    }
                }
            });
        });


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
                        GroupsBean gb = new GroupsBean();
                        if (editGroupDialogInput.getText().toString() != null || !editGroupDialogInput.getText().toString().equals("")) {
                            gb.setName(editGroupDialogInput.getText().toString());
                            gb.setUserId(getActivity().getIntent().getStringExtra("userId"));
                            AsyncTaskHelper.execute(() -> GroupsServiceImpl.add(gb), groupsBean -> {
                                LayoutInflater chipInflater = LayoutInflater.from(getActivity());
                                Chip chip = new Chip(getActivity());
                                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getActivity(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                                chip.setChipDrawable(chipDrawable);
                                chip.setText(gb.getName());
                                chipGroup.addView(chip);
                                if (alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "未輸入群組名稱", Toast.LENGTH_SHORT).show();
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
                FriendGroupBean updateFriendGroupBean = new FriendGroupBean();
                updateFriendGroupBean.setFriendGroupNo(friendGroupNo);
                updateFriendGroupBean.setGroupNo(updateGroupNo);
                updateFriendGroupBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                System.out.println("updateFriendGroupBean.getFriendGroupNo() = " + updateFriendGroupBean.getFriendGroupNo());
                System.out.println("updateFriendGroupBean.getGroupNo() = " + updateFriendGroupBean.getGroupNo());
                System.out.println("updateFriendGroupBean.getFriendNo() = " + updateFriendGroupBean.getFriendNo());
                AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.update(updateFriendGroupBean), friendGroupBean -> {
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