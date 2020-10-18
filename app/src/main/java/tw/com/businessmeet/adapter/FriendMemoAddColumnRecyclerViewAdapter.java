package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;

public class FriendMemoAddColumnRecyclerViewAdapter extends RecyclerView.Adapter<FriendMemoAddColumnRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<FriendCustomizationBean> friendCustomizationBeanList;
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();

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
        String content = data.getContent();
        if (content != null) {
            int contentLength = 0;
            contentLength = friendCustomizationBeanList.get(position).getContent().length();
            String[] chipContent = new String[contentLength];
            chipContent = content.split(",");
            for (int j = 1; j < chipContent.length; j++) {
                Chip chip = new Chip(context);
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                chip.setChipDrawable(chipDrawable);
                chip.setText(chipContent[j]);
                chip.setCloseIconVisible(true);
                holder.chipGroup.addView(chip);
            }
        }

    }

    // 得到總條數
    @Override
    public int getItemCount() {
        return friendCustomizationBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memoTitle;
        private ChipGroup chipGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTitle = (TextView) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_column_title);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.friends_edit_profile_memo_chip_group);
        }
    }
}
