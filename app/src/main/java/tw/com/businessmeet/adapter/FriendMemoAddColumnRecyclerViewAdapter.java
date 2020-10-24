package tw.com.businessmeet.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import tw.com.businessmeet.FriendSearchActivity;
import tw.com.businessmeet.FriendsIntroductionActivity;
import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendGroupServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class FriendMemoAddColumnRecyclerViewAdapter extends RecyclerView.Adapter<FriendMemoAddColumnRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private ClickListener clickListener;
    private Integer friendCustomizationNo;
    private List<FriendCustomizationBean> friendCustomizationBeanList;

    //創建構造函數
    public FriendMemoAddColumnRecyclerViewAdapter(Context context, List<FriendCustomizationBean> friendCustomizationBeanList, ClickListener clickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.clickListener = clickListener;
        this.friendCustomizationBeanList = friendCustomizationBeanList;
    }

    @NonNull
    @Override
    public FriendMemoAddColumnRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 創建自定義布局
        View view = layoutInflater.inflate(R.layout.recycler_view_addcolumn_memo, parent, false);
        return new ViewHolder(view, clickListener);
    }

    // 得到總條數
    @Override
    public int getItemCount() {
        return friendCustomizationBeanList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendMemoAddColumnRecyclerViewAdapter.ViewHolder holder, int position) {
        FriendCustomizationBean data = friendCustomizationBeanList.get(position);
        holder.memoTitle.setText(data.getName());
        friendCustomizationNo = data.getFriendCustomizationNo();
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
                holder.chipGroup.addView(chip);
            }
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(friendCustomizationNo);
                    AsyncTaskHelper.execute(() -> FriendCustomizationServiceImpl.delete(friendCustomizationNo), empty -> {
                        friendCustomizationBeanList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, friendCustomizationBeanList.size());
                        System.out.println("!!!deleteButton success!!!");

                    });
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView memoTitle;
        private ChipGroup chipGroup;
        private ImageButton deleteButton;
        private ClickListener clickListener;

        public ViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            memoTitle = (TextView) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_column_title);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.friends_edit_profile_memo_chip_group);
            deleteButton = (ImageButton) itemView.findViewById(R.id.friends_edit_profile_memo_recycleView_column_removeColumn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
            if (clickListener != null) {
                clickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ClickListener clickLinster) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

    public FriendCustomizationBean getFriendMemo(int position) {
        return friendCustomizationBeanList.get(position);
    }
}
