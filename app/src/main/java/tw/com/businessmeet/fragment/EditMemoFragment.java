package tw.com.businessmeet.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.activity.EditFriendMemoActivity;
import tw.com.businessmeet.adapter.FriendMemoAddColumnRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;

public class EditMemoFragment extends Fragment implements FriendMemoAddColumnRecyclerViewAdapter.ClickListener {
    private View view;
    private RecyclerView recyclerViewMemo;
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private List<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();
    private FriendMemoAddColumnRecyclerViewAdapter friendMemoAddColumnRecyclerViewAdapter;

    // 新增備註欄位對話框
    private EditText addColumnMemo, addChipMemo;
    private ChipGroup chipGroup;
    private Button confirm, cancel;
    private ArrayList<String> originalChipContent, deleteChipContent, updateChipContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_memo, container, false);
        FriendCustomizationBean friendCustomizationBean = new FriendCustomizationBean();
        friendCustomizationBean.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        friendSearchMemo(friendCustomizationBean);
        // 所有備註用recyclerView顯示
        recyclerViewMemo = (RecyclerView) view.findViewById(R.id.friends_edit_profile_memo_recycleView);
        initMemoRecyclerView();
        // 新增欄位floating btn
        extendedFloatingActionButton = (ExtendedFloatingActionButton) view.findViewById(R.id.memo_addColumn);
        extendedFloatingActionButton.setOnClickListener(dialogClick);

        return view;
    }

    // 搜尋所有備註
    private void friendSearchMemo(FriendCustomizationBean friendNo) {
        AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.search(friendNo), fcbl -> {
            for (int i = 0; i < fcbl.size(); i++) {
                if (fcbl.get(i).getFriendNo() == getActivity().getIntent().getIntExtra("friendNo", 0)) {
                    friendCustomizationBeanList.add(fcbl.get(i));
                }
            }
            initMemoRecyclerView();
        });
    }

    // recyclerView呈現
    private void initMemoRecyclerView() {
        // 創建adapter
        friendMemoAddColumnRecyclerViewAdapter = new FriendMemoAddColumnRecyclerViewAdapter(getContext(), friendCustomizationBeanList, this);
        // recycleView設置adapter
        recyclerViewMemo.setAdapter(friendMemoAddColumnRecyclerViewAdapter);
        // 點擊後編輯
        friendMemoAddColumnRecyclerViewAdapter.setClickListener(this::onClick);
        // 設置layoutManager，可以設置顯示效果(線性布局、grid布局、瀑布流布局)
        // 參數:上下文、列表方向(垂直Vertical/水平Horizontal)、是否倒敘
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 設置item的分割線
        recyclerViewMemo.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    // 進入編輯畫面
    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(getActivity(), EditFriendMemoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("friendCustomizationNo", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getFriendCustomizationNo());
        bundle.putInt("friendNo", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getFriendNo());
        bundle.putString("friendId", getActivity().getIntent().getStringExtra("friendId"));
        bundle.putString("name", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getName());
        bundle.putString("content", friendMemoAddColumnRecyclerViewAdapter.getFriendMemo(position).getContent());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // 點選floating btn跳出新增欄位對話框
    public View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 跳出對話框
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = inflater.inflate(R.layout.friend_add_column, null);
            builder.setView(view);
            builder.create();
            AlertDialog alertDialog = builder.show();
            addColumnMemo = (EditText) view.findViewById(R.id.addColumn_dialog_Input);
            addChipMemo = (EditText) view.findViewById(R.id.addTag_dialog_Input);
            chipGroup = (ChipGroup) view.findViewById(R.id.addTag_dialog_selectedBox);
            confirm = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
            cancel = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
            originalChipContent = new ArrayList<String>();
            deleteChipContent = new ArrayList<String>();
            addChipMemo.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        originalChipContent.add(addChipMemo.getText().toString());
                        chipGroup.addView(createChip(addChipMemo.getText().toString()));
                    } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                        addChipMemo.setText("");
                    }
                    return false;
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FriendCustomizationBean fcb = new FriendCustomizationBean();
                    fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                    fcb.setName(addColumnMemo.getText().toString());
                    if (deleteChipContent.size() == 0) {
                        String originalChipContentString = "";
                        for (int i = 0; i < originalChipContent.size(); i++) {
                            originalChipContentString = originalChipContentString + "," + originalChipContent.get(i);
                        }
                        fcb.setContent(originalChipContentString);
                        insertData(fcb, originalChipContentString, alertDialog);
                    } else {
                        updateChipContent = new ArrayList<String>();
                        for (int i = 0; i < originalChipContent.size(); i++) {
                            int count = 0;
                            for (int j = 0; j < deleteChipContent.size(); j++) {
                                if (originalChipContent.get(i).equals(deleteChipContent.get(j))) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                updateChipContent.add(originalChipContent.get(i));
                            }
                        }
                        String updateChipContentString = "";
                        for (int i = 0; i < updateChipContent.size(); i++) {
                            updateChipContentString = updateChipContentString + "," + updateChipContent.get(i);
                        }
                        insertData(fcb, updateChipContentString, alertDialog);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog(alertDialog);
                }
            });
        }
    };

    // 建立chip
    public Chip createChip(String chipContent) {
        Chip chip = new Chip(getContext());
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
        chip.setChipDrawable(chipDrawable);
        chip.setText(chipContent);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChipContent.add(chip.getText().toString());
                chipGroup.removeView(chip);
            }
        });
        return chip;
    }

    //新增備註內容到資料庫
    private void insertData(FriendCustomizationBean friendCustomizationBean, String content, AlertDialog alertDialog) {
        friendCustomizationBean.setContent(content);
        if (checkData(friendCustomizationBean)) {
            AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.add(friendCustomizationBean), fcb -> {
                friendSearchMemo(fcb);
            });
            closeDialog(alertDialog);
        }
    }

    // 檢查資料是否輸入正確
    private boolean checkData(FriendCustomizationBean friendCustomizationBean) {
        if (friendCustomizationBean.getName() == null || friendCustomizationBean.getName().equals("")) {
            Toast.makeText(getContext(), "未輸入欄位名稱", Toast.LENGTH_LONG).show();
        } else if ((friendCustomizationBean.getContent() == null || friendCustomizationBean.getContent().equals("")) && (addChipMemo == null || addChipMemo.getText().toString().equals(""))) {
            Toast.makeText(getContext(), "未輸入備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().length() >= 1) {
            Toast.makeText(getContext(), "備註輸入完請按Enter確認變成標籤後，才能夠新增備註", Toast.LENGTH_LONG).show();
        } else if (addChipMemo.getText().toString().equals("")) {
            return true;
        } else {
            return true;
        }
        return false;
    }

    // 關閉新增欄位對話框
    private void closeDialog(AlertDialog alertDialog) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}