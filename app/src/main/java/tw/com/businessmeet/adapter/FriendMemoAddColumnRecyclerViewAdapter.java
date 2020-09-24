package tw.com.businessmeet.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.UserInformationBean;

public class FriendMemoAddColumnRecyclerViewAdapter extends RecyclerView.Adapter<FriendMemoAddColumnRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<FriendCustomizationBean> friendCustomizationBeanList;
    private FriendsRecyclerViewAdapter.ClickListener clickLinster;

    //創建構造函數
    public FriendMemoAddColumnRecyclerViewAdapter(Context context, List<FriendCustomizationBean> friendCustomizationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendCustomizationBeanList = friendCustomizationBeanList;
    }

    @NonNull
    @Override
    public FriendMemoAddColumnRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 創建自定義布局
        View view = layoutInflater.inflate(R.layout.recycler_view_addcolumn_memo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendMemoAddColumnRecyclerViewAdapter.ViewHolder holder, int position) {
        FriendCustomizationBean data = friendCustomizationBeanList.get(position);
        holder.memoTitle.setText(data.getName());
    }

    // 得到總條數
    @Override
    public int getItemCount() {
        return friendCustomizationBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memoTitle;
        private EditText chipInput;
        private ChipGroup chipGroup;
        private Chip chip;
        private String chipContent;
        private ArrayList<String> friendLabelChipList = new ArrayList<>();
        private int spannedLength = 0, deleteLength = 0, friendLabelChipListIndex = 0, onStart = 0;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTitle = (TextView) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_column_title);
            // chip
//            chipGroup = (ChipGroup) itemView.findViewById(R.id.friends_edit_profile_memo_chip_group);
            chipInput = (EditText) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_tag_input);
//            chipInput.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                        chipContent = chipInput.getText().toString();
//                        ChipDrawable chip = ChipDrawable.createFromResource(context, R.xml.recycler_view_chip);
//                        chip.setText(chipContent);
//                        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
//                        ImageSpan imageSpan = new ImageSpan(chip);
//                        Editable s = null;
//                        s.setSpan(imageSpan, 0, chipContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }
//                    return false;
//                }
//            });

            chipInput.addTextChangedListener(new TextWatcher() {
                @Override
                // s:原本的內容、start:新輸入內容開始的地方、count:輸入的內容要替代的字符數(輸入count=0、刪除count=1)、after:新輸入的字符數
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    System.out.println("beforeTextChanged: charSequence=" + s + ", start=" + start + ", count=" + count + ", after=" + after);
                    onStart = start;
                }

                @Override
                // s:輸入內容後的文字、start:新輸入字符的位置、before:輸入之前這個位置的字符數、count:輸入內容的字符數
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println("onTextChanged: charSequence=" + s + ", start=" + start + ", before=" + before + ", count=" + count);

//                    chipInput.setOnKeyListener(new View.OnKeyListener() {
//                        @Override
//                        public boolean onKey(View v, int keyCode, KeyEvent event) {
//                            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
//                                spannedLength = s.length() - 1;
//                                System.out.println("afterTextChanged: 3spannedLength=" + spannedLength);
//                                return true;
//                            }
//                            return false;
//                        }
//                    });
                }

                @Override
                public void afterTextChanged(Editable s) {
                    chipInput.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                                ChipDrawable chip = ChipDrawable.createFromResource(context, R.xml.recycler_view_chip);
                                chip.setText(s.subSequence(spannedLength, s.length()));
                                chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                                ImageSpan imageSpan = new ImageSpan(chip);
                                s.setSpan(imageSpan, spannedLength, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                System.out.println("afterTextChanged: spannedLength=" + spannedLength + " " + "sLength=" + s.length());
                                spannedLength = s.length();
                                if (s.length() > 0 && (s.toString() != "" || s.toString() != null)) {
                                    friendLabelChipList.add(chip.getText().toString());
                                    System.out.println("afterTextChanged: " + friendLabelChipList.get(friendLabelChipListIndex));
                                    friendLabelChipListIndex = friendLabelChipListIndex + 1;
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                }
            });
        }
    }
}
