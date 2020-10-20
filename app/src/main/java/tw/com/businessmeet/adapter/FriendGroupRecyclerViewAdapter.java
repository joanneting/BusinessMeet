package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;


public class FriendGroupRecyclerViewAdapter extends RecyclerView.Adapter<FriendGroupRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<FriendGroupBean> friendGroupBeanList;
    private ClickListener clickLinster;
    private AvatarHelper avatarHelper = new AvatarHelper();
    public FriendGroupRecyclerViewAdapter(Context context, List<FriendGroupBean> friendGroupBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendGroupBeanList = friendGroupBeanList;
    }

    @NonNull
    @Override
    public FriendGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friend_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendGroupRecyclerViewAdapter.ViewHolder holder, int position) {
        FriendGroupBean friendGroupBean = friendGroupBeanList.get(position);
        holder.bindInformation(friendGroupBean.getGroupName(),friendGroupBean.getCount().toString());
    }

    @Override
    public int getItemCount() {
        return friendGroupBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupName;
        TextView groupCount;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group);
            groupCount = itemView.findViewById(R.id.group_count);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String name, String count){
            groupName.setText(name);
            groupCount.setText(count);
        }

        @Override
        public void onClick(View v) {
            if(clickLinster != null){
                clickLinster.onClick(v,getAdapterPosition());
            }
        }

    }
    public FriendGroupBean getFriendGroupBean(int position){
        return friendGroupBeanList.get(position);
    }
    public void  setClickListener(ClickListener clickLinster){
        this.clickLinster = clickLinster;
    }
    public void dataInsert(FriendGroupBean friendGroupBean){
        friendGroupBeanList.add(friendGroupBean);
        notifyItemInserted(getItemCount());
    }
    public interface ClickListener{
        void onClick(View view, int position);
    }

}