package tw.com.businessmeet.adapter;

import android.content.Context;
import android.text.Editable;
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

import com.google.android.material.chip.ChipDrawable;

import org.w3c.dom.Text;

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
        private int spannedLength = 0, chipLength = 4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTitle = (TextView) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_column_title);
            chipInput = (EditText) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_tag_input);
            // chip
            chipInput.addTextChangedListener(new TextWatcher() {
                @Override
                // s:原本的內容、start:新輸入內容開始的地方、count:輸入的內容要替代的字符數(輸入count=0、刪除count=1)、after:新輸入的字符數
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                // s:輸入內容後的文字、start:新輸入字符的位置、before:輸入之前這個位置的字符數、count:輸入內容的字符數
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == spannedLength - chipLength) {
                        spannedLength = s.length();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    chipInput.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                chipLength = s.length();
                                ChipDrawable chip = ChipDrawable.createFromResource(context, R.xml.recycler_view_chip);
                                chip.setText(s.subSequence(spannedLength, s.length()));
                                chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                                ImageSpan imageSpan = new ImageSpan(chip);
                                s.setSpan(imageSpan, spannedLength, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannedLength = s.length();
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
