package tw.com.businessmeet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.GroupsServiceImpl;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private Chip currentGroupChip;
    private ChipGroup chipGroup;
    private EditText remark;
    private Button editProfileConfirmBtn;

    private Integer currentFriendGroupNo, currentGroupNo;
    private String remarkContent;

    private List<GroupsBean> groupsBeansList = new ArrayList<>();
    private Boolean currentGroupChipFlag = false;

    public EditProfileFragment() {
    }

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
        currentGroupChip = (Chip) view.findViewById(R.id.currentGroupTitleChip);
        chipGroup = (ChipGroup) view.findViewById(R.id.chooseGroup_chipGroup);
        remark = (EditText) view.findViewById(R.id.addProfileContent_input);
        remark.append(getActivity().getIntent().getStringExtra("remark"));
        editProfileConfirmBtn = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);

        // 頁面一進去一開始搜尋好友目前群組
        FriendGroupBean friendGroupBean = new FriendGroupBean();
        friendGroupBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        searchFriendGroup(friendGroupBean);
        // 頁面一進去一開始搜尋使用者新增的所有群組
        GroupsBean groupsBean = new GroupsBean();
        groupsBean.setUserId(getActivity().getIntent().getStringExtra("userId"));
        searchAllGroup(groupsBean);
        // 更新編輯後的整個頁面
        editProfileConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemark();
                updateFriendGroup(friendGroupBean);
            }
        });

        return view;
    }

    // 頁面一進去搜尋好友目前群組
    private void searchFriendGroup(FriendGroupBean friendGroupBean) {
        AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.search(friendGroupBean), fgb -> {
            if (fgb.size() > 0) {
                currentFriendGroupNo = fgb.get(0).getFriendGroupNo();
                currentGroupNo = fgb.get(0).getGroupNo();
            }
        });
    }

    // 頁面一進去搜尋使用者所有群組
    private void searchAllGroup(GroupsBean groupsBean) {
        AsyncTaskHelper.execute(() -> GroupsServiceImpl.search(groupsBean), gbl -> {
            for (int i = 0; i < gbl.size(); i++) {
                groupsBeansList.add(gbl.get(i));
                if (currentGroupNo == gbl.get(i).getGroupNo()) {
                    currentCloseIconOnclick(currentGroupChip, gbl.get(i).getName(), gbl.get(i).getGroupNo());
                }
            }
            createGroupChip(groupsBeansList);
        });
    }

    // 所有新增的群組變成chip
    private void createGroupChip(List<GroupsBean> groupsBeansList) {
        if (groupsBeansList != null) {
            for (int i = 0; i < groupsBeansList.size(); i++) {
                LayoutInflater chipInflater = LayoutInflater.from(getActivity());
                Chip chip = new Chip(getActivity());
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getActivity(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                chip.setChipDrawable(chipDrawable);
                chip.setId(groupsBeansList.get(i).getGroupNo());
                chip.setText(groupsBeansList.get(i).getName());
                chip.setCloseIconVisible(true);
                chooseCurrentGroup(chip);
                chipGroup.addView(chip);
            }
        }
    }

    // 所有新增的群組變成chip-設定好友目前群組
    private void chooseCurrentGroup(Chip chip) {
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), chip.getText().toString() + " " + chip.getId(), Toast.LENGTH_SHORT).show();
                currentCloseIconOnclick(currentGroupChip, chip.getText().toString(), chip.getId());
            }
        });
    }

    // 新增備忘錄
    private void addRemark() {
        FriendBean friendBean = new FriendBean();
        remarkContent = remark.getText().toString();
        friendBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        friendBean.setMatchmakerId(getActivity().getIntent().getStringExtra("userId"));
        friendBean.setFriendId(getActivity().getIntent().getStringExtra("friendId"));
        friendBean.setRemark(remarkContent);
        AsyncTaskHelper.execute(() -> FriendServiceImpl.update(friendBean), fb -> {
            System.out.println("!!!add remark success!!!");
        });
    }

    // current group close action
    private void currentCloseIconOnclick(Chip chip, String text, Integer groupNo) {
        chip.setCloseIconVisible(true);
        chip.setText(text);
        chip.setId(groupNo);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGroupChipFlag = true;
                chip.setText("");
                chip.setCloseIconVisible(false);
            }
        });
    }

    //編輯群組名稱



    // 新增、編輯、刪除好友群組
    private void updateFriendGroup(FriendGroupBean friendGroupBean) {
        friendGroupBean.setGroupNo(currentGroupChip.getId());
        if (currentFriendGroupNo == null) {
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.add(friendGroupBean), fgb -> {
                System.out.println("!!!update friend group - add success!!!");
            });
        } else {
            friendGroupBean.setFriendGroupNo(currentFriendGroupNo);
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.update(friendGroupBean), fgb -> {
                System.out.println("!!!update friend group - update success!!!");
            });
        }
        if (currentGroupChipFlag == true) {
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.delete(currentFriendGroupNo), fgb -> {
                System.out.println("!!!current close icon onclick success!!!");
            });
        }
        getActivity().finish();
    }
}