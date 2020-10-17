package tw.com.businessmeet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

import tw.com.businessmeet.adapter.FriendMemoAddColumnRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;

public class EditMemoFragment extends Fragment {
    private View view;

    // floating button
    private ExtendedFloatingActionButton extendedFloatingActionButton;

    //chip
    private ChipGroup chipGroup;
    private String chipContent;

    //edit
    private ImageButton editButton;

    // MemoFragment
    private final FriendCustomizationBean fcb = new FriendCustomizationBean();
    private RecyclerView recyclerViewMemo;
    private List<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();
    private FriendMemoAddColumnRecyclerViewAdapter friendMemoAddColumnRecyclerViewAdapter;

    // dialog
    private Button confirm, cancel;
    private EditText addColumnMemo;
    private EditText addChipMemo;
    private FriendCustomizationDAO friendCustomizationDAO;
    private DBHelper dh = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_memo, container, false);
        fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.search(fcb), friendCustomizationBeanList -> {
            if (friendCustomizationBeanList.size() > 1 ||
                    (friendCustomizationBeanList.size() == 1 && friendCustomizationBeanList.get(0).getCreateDate() != null)) {
                EditMemoFragment.this.friendCustomizationBeanList.addAll(friendCustomizationBeanList);
            }
        });

        // recyclerView
        recyclerViewMemo = (RecyclerView) view.findViewById(R.id.friends_edit_profile_memo_recycleView);
        initMemoRecyclerView();

        // floating button
        extendedFloatingActionButton = (ExtendedFloatingActionButton) view.findViewById(R.id.memo_addColumn);
        extendedFloatingActionButton.setOnClickListener(dialogClick);

        return view;
    }

    private void openDB() {
        dh = new DBHelper(getContext());
        friendCustomizationDAO = new FriendCustomizationDAO(dh);
    }

    private void initMemoRecyclerView() {
        // 創建adapter
        friendMemoAddColumnRecyclerViewAdapter = new FriendMemoAddColumnRecyclerViewAdapter(getContext(), friendCustomizationBeanList);
        // recycleView設置adapter
        recyclerViewMemo.setAdapter(friendMemoAddColumnRecyclerViewAdapter);
        // 設置layoutManager，可以設置顯示效果(線性布局、grid布局、瀑布流布局)
        // 參數:上下文、列表方向(垂直Vertical/水平Horizontal)、是否倒敘
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 設置item的分割線
        recyclerViewMemo.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //點擊進入編輯
        recyclerViewMemo.setOnClickListener(dialogClick);
    }

    public View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = inflater.inflate(R.layout.friend_add_column, null);
            builder.setView(view);
            builder.create();
            AlertDialog alertDialog = builder.show();
            addColumnMemo = view.findViewById(R.id.addColumn_dialog_Input);
            addChipMemo = view.findViewById(R.id.addTag_dialog_Input);
            chipGroup = view.findViewById(R.id.addTag_dialog_selectedBox);
            addChipMemo.setOnKeyListener((v13, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    chipContent = chipContent + "," + addChipMemo.getText().toString();
                    LayoutInflater chipInflater = LayoutInflater.from(getContext());
                    Chip chip = new Chip(getContext());
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                    chip.setChipDrawable(chipDrawable);
                    chip.setText(addChipMemo.getText().toString());
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v13) {
                            chipGroup.removeView(chip);
                        }
                    });
                    chipGroup.addView(chip);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    addChipMemo.setText("");
                    return true;
                }
                return false;
            });

            confirm = view.findViewById(R.id.addColumn_dialog_confirmButton);
            confirm.setOnClickListener(v1 -> {
                fcb.setName(addColumnMemo.getText().toString());
                fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                fcb.setContent(chipContent);
                chipContent = "";
                openDB();
                friendCustomizationDAO.add(fcb);
                AsyncTaskHelper.execute(
                        () -> FriendCustomizationServiceImpl.add(fcb),
                        friendCustomizationBean ->
                                AsyncTaskHelper.execute(() ->
                                        FriendCustomizationServiceImpl.add(fcb)
                                )
                );
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            });
            cancel = view.findViewById(R.id.addColumn_dialog_cancelButton);
            cancel.setOnClickListener(v12 -> {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            });
        }
    };
}
