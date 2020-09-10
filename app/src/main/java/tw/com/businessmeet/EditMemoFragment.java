package tw.com.businessmeet;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.adapter.FriendMemoAddColumnRecyclerViewAdapter;
import tw.com.businessmeet.adapter.FriendMemoTagRecyclerViewAdapter;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.FriendLabelBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMemoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    // floating button
    private FloatingActionButton floatingActionButton;

    //chip
    private Chip chips;
    private FriendMemoTagRecyclerViewAdapter friendMemoTagRecyclerViewAdapter;
    private ArrayList<FriendLabelBean> friendLabelBeanList = new ArrayList<FriendLabelBean>();
    // MemoFragment
    private FriendCustomizationBean fcb = new FriendCustomizationBean();
    private RecyclerView recyclerViewMemo;
    private ArrayList<FriendCustomizationBean> friendCustomizationBeanList = new ArrayList<FriendCustomizationBean>();
    private FriendMemoAddColumnRecyclerViewAdapter friendMemoAddColumnRecyclerViewAdapter;

    // dialog
    private Button confirm, cancel;
    private EditText addColumnMemo;
    private FriendCustomizationDAO friendCustomizationDAO;
    private DBHelper dh = null;
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean>() {
                @Override
                public Call<ResponseBody<FriendCustomizationBean>> request(FriendCustomizationBean... friendCustomizationBean) {
                    return friendCustomizationServiceImpl.add(friendCustomizationBean[0]);
                }

                @Override
                public void onSuccess(FriendCustomizationBean friendCustomizationBean) {
                }

                @Override
                public void onFail(int status, String message) {
                }
            };

    //column
    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, List<FriendCustomizationBean>> searchResponseListener = new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, List<FriendCustomizationBean>>() {

        @Override
        public Call<ResponseBody<List<FriendCustomizationBean>>> request(FriendCustomizationBean... friendCustomizationBeans) {
            return friendCustomizationServiceImpl.search(friendCustomizationBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendCustomizationBean> friendCustomizationBeans) {
            Log.d("MemoTitle", "count:" + friendCustomizationBeans.size());
            if (friendCustomizationBeans.size() > 1 || (friendCustomizationBeans.size() == 1 && (friendCustomizationBeans.get(0).getCreateDate() != null && !friendCustomizationBeans.get(0).equals("")))) {
                for (int i = 0; i < friendCustomizationBeans.size(); i++) {
                    friendCustomizationBeanList.add(friendCustomizationBeans.get(i));
                    Log.d("TitleName", i + ":" + friendCustomizationBeanList.get(i).getName());
                }
            }
        }

        @Override
        public void onFail(int status, String message) {
        }
    };

    public EditMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_editMemo.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMemoFragment newInstance(String param1, String param2) {
        EditMemoFragment fragment = new EditMemoFragment();
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
        fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
        Log.d("fcb", "No:" + fcb.getFriendNo());
        AsyncTasKHelper.execute(searchResponseListener, fcb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_memo, container, false);
        // recyclerView
        recyclerViewMemo = (RecyclerView) view.findViewById(R.id.friends_edit_profile_memo_recycleView);
        initMemoRecyclerView();
        // floating button
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.memo_addColumn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog
                Log.d("EditMemoFragment", "friendNo:" + getActivity().getIntent().getIntExtra("friendNo", 0));
                LayoutInflater inflater = getActivity().getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = inflater.inflate(R.layout.add_column, null);
                builder.setView(view);
                builder.create();
                AlertDialog alertDialog = builder.show();

                addColumnMemo = (EditText) view.findViewById(R.id.addColumn_dialog_Input);
                confirm = (Button) view.findViewById(R.id.addColumn_dialog_confirmButton);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fcb.setName(addColumnMemo.getText().toString());
                        fcb.setFriendNo(getActivity().getIntent().getIntExtra("friendNo", 0));
                        openDB();
                        friendCustomizationDAO.add(fcb);
                        AsyncTasKHelper.execute(addResponseListener, fcb);
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                cancel = (Button) view.findViewById(R.id.addColumn_dialog_cancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        return view;
    }

    private void openDB() {
        dh = new DBHelper(getContext());
        friendCustomizationDAO = new FriendCustomizationDAO(dh);
    }

    private void initMemoRecyclerView() {
        // 創建adapter
        friendMemoAddColumnRecyclerViewAdapter = new FriendMemoAddColumnRecyclerViewAdapter(getActivity(), friendCustomizationBeanList);
        // recycleView設置adapter
        recyclerViewMemo.setAdapter(friendMemoAddColumnRecyclerViewAdapter);
        // 設置layoutManager，可以設置顯示效果(線性布局、grid布局、瀑布流布局)
        // 參數:上下文、列表方向(垂直Vertical/水平Horizontal)、是否倒敘
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // 設置item的分割線
        recyclerViewMemo.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
}
