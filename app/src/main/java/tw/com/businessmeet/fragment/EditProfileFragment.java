package tw.com.businessmeet.fragment;

import android.app.AlertDialog;
import android.content.Intent;
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

import tw.com.businessmeet.R;
import tw.com.businessmeet.activity.FriendsIntroductionActivity;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.FriendGroupDAO;
import tw.com.businessmeet.dao.GroupsDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.GroupsServiceImpl;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // 主頁面
    private View view;
    private Chip currentGroupChip, addGroupChip;
    private ChipGroup chipGroup;
    private EditText remark;
    private Button editProfileConfirmBtn;
    // 新增群組對話框
    private EditText editGroupDialogInput;
    private Button editGroupDialogConfirmBtn, editGroupDialogcancelBtn;
    // 編輯群組對話框
    private EditText renameGroupDialogInput;
    private Button renameGroupDialogConfirmBtn, renameGroupDialogDeleteBtn, renameGroupDialogCancelBtn;

    private Integer currentFriendGroupNo, currentGroupNo;
    private String remarkContent;

    private List<GroupsBean> groupsBeansList = new ArrayList<>();
    private Boolean currentGroupChipFlag = false;
    private FriendDAO friendDAO;
    private DBHelper dbHelper;
    private GroupsDAO groupsDAO;
    private FriendGroupDAO friendGroupDAO;

    public EditProfileFragment() {
    }

    private void openDB() {
        dbHelper = new DBHelper(getActivity());
        friendDAO = new FriendDAO(dbHelper);
        groupsDAO = new GroupsDAO(dbHelper);
        friendGroupDAO = new FriendGroupDAO(dbHelper);
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
        addGroupChip = (Chip) view.findViewById(R.id.addGroupButton);
        chipGroup = (ChipGroup) view.findViewById(R.id.chooseGroup_chipGroup);
        remark = (EditText) view.findViewById(R.id.addProfileContent_input);
        if (getActivity().getIntent().getStringExtra("remark") != null) {
            remark.append(getActivity().getIntent().getStringExtra("remark"));
        }
        editProfileConfirmBtn = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
        // 頁面一進去一開始搜尋好友目前群組
        FriendGroupBean friendGroupBean = new FriendGroupBean();
        friendGroupBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        searchFriendGroup(friendGroupBean);
        // 頁面一進去一開始搜尋使用者新增的所有群組
        GroupsBean groupsBean = new GroupsBean();
        groupsBean.setUserId(DeviceHelper.getUserId(getContext()));
        searchAllGroup(groupsBean);
        // 新增群組
        addGroup();
        // 更新編輯後的整個頁面
        editProfileConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemark();
                updateFriendGroup(friendGroupBean);
            }
        });
        openDB();
        return view;
    }

    // 頁面一進去搜尋好友目前群組
    private void searchFriendGroup(FriendGroupBean friendGroupBean) {
        AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.search(friendGroupBean), fgb -> {
            if (fgb.size() > 0) {
                currentFriendGroupNo = fgb.get(0).getFriendGroupNo();
                currentGroupNo = fgb.get(0).getGroupNo();
                System.out.println("!!!search friend group success!!!");
            }
        });
    }

    // 頁面一進去搜尋使用者所有群組
    private void searchAllGroup(GroupsBean groupsBean) {
        AsyncTaskHelper.execute(() -> GroupsServiceImpl.search(groupsBean), gbl -> {
            for (int i = 0; i < gbl.size(); i++) {
                System.out.println("gbl.get(i).getUserId() = " + gbl.get(i).getUserId());
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
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("chip.getId() = " + chip.getId());
                        editGroup(chip.getId(), chip);
                    }
                });
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
        AsyncTaskHelper.execute(() -> FriendServiceImpl.update(friendBean), friendDAO::update);
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

    //新增群組
    private void addGroup() {
        addGroupChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = inflater.inflate(R.layout.friend_edit_group, null);
                builder.setView(view);
                builder.create();
                AlertDialog alertDialog = builder.show();
                editGroupDialogInput = (EditText) view.findViewById(R.id.editGroup_dialog_Input);
                editGroupDialogConfirmBtn = (Button) view.findViewById(R.id.editGroup_dialog_confirmButton);
                editGroupDialogConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editGroupDialogInput.getText().toString() != null || !editGroupDialogInput.getText().toString().equals("")) {
                            GroupsBean groupsBean = new GroupsBean();
                            groupsBean.setUserId(getActivity().getIntent().getStringExtra("userId"));
                            groupsBean.setName(editGroupDialogInput.getText().toString());
                            AsyncTaskHelper.execute(() -> GroupsServiceImpl.add(groupsBean), gb -> {
                                groupsDAO.add(gb);
                                List<GroupsBean> groupsBeanList = new ArrayList<>();
                                groupsBeanList.add(gb);
                                createGroupChip(groupsBeanList);
                                if (alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "未輸入群組名稱", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //刪除、編輯群組
    private void editGroup(Integer groupNo, Chip chip) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.friend_editndelete_group, null);
        builder.setView(view);
        builder.create();
        AlertDialog alertDialog = builder.show();
        renameGroupDialogInput = (EditText) view.findViewById(R.id.editGroup_dialog_Input);
        renameGroupDialogInput.append(chip.getText());
        renameGroupDialogConfirmBtn = (Button) view.findViewById(R.id.editGroup_dialog_confirmButton);
        renameGroupDialogConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (renameGroupDialogInput.getText().toString() != null || !renameGroupDialogInput.getText().toString().equals("")) {
                    GroupsBean groupsBean = new GroupsBean();
                    groupsBean.setGroupNo(groupNo);
                    groupsBean.setUserId(getActivity().getIntent().getStringExtra("userId"));
                    groupsBean.setName(renameGroupDialogInput.getText().toString());
                    AsyncTaskHelper.execute(() -> GroupsServiceImpl.update(groupsBean), gb -> {
                        chip.setText(renameGroupDialogInput.getText().toString());
                        groupsDAO.update(gb);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "未輸入群組名稱", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 刪除群組
        renameGroupDialogDeleteBtn = (Button) view.findViewById(R.id.editGroup_dialog_deleteButton);
        renameGroupDialogDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.searchFriendByGroup(groupNo), fgl -> {
                    if (fgl.size() == 0) {
                        AsyncTaskHelper.execute(() -> GroupsServiceImpl.delete(groupNo), empty -> {
                            groupsDAO.delete(groupNo);
                            chipGroup.removeView(chip);
                            closeDialog(alertDialog);
                        });
                    } else {
                        for (int i = 0; i < fgl.size(); i++) {
                            int finalI = i;
                            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.delete(fgl.get(finalI).getFriendGroupNo()), empty -> {
                                friendGroupDAO.delete(fgl.get(finalI).getFriendGroupNo());
                                chipGroup.removeView(chip);
                                System.out.println("!!!delete friendGroupNo=" + fgl.get(finalI).getFriendGroupNo() + "success");
                                closeDialog(alertDialog);
                            });
                        }
                    }
                });
            }
        });
        // 取消編輯
        renameGroupDialogCancelBtn = (Button) view.findViewById(R.id.editGroup_dialog_cancelButton);
        renameGroupDialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog(alertDialog);
            }
        });
    }

    // 關閉對話框
    private void closeDialog(AlertDialog alertDialog) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    // 新增、編輯、刪除好友群組
    private void updateFriendGroup(FriendGroupBean friendGroupBean) {
        friendGroupBean.setGroupNo(currentGroupChip.getId());
        System.out.println("currentFriendGroupNo = " + currentFriendGroupNo);
        if (currentFriendGroupNo == null) {
            System.out.println("\"add\" = " + "add");
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.add(friendGroupBean), friendGroupDAO::add);
        } else {
            friendGroupBean.setFriendGroupNo(currentFriendGroupNo);
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.update(friendGroupBean), friendGroupDAO::update);
        }
        if (currentGroupChipFlag == true) {
            AsyncTaskHelper.execute(() -> FriendGroupServiceImpl.delete(currentFriendGroupNo), fgb -> {
                friendGroupDAO.delete(currentFriendGroupNo);
            });
        }
        changePage();
    }

    //跳頁傳送資料
    public void changePage() {
        Intent intent = new Intent(getActivity(), FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        System.out.println(getActivity().getIntent().getStringExtra("friendId"));
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}