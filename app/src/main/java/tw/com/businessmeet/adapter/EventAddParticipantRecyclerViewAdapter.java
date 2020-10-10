package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.RecyclerViewFilterBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;

public class EventAddParticipantRecyclerViewAdapter extends RecyclerView.Adapter<EventAddParticipantRecyclerViewAdapter.ViewHolder> implements Filterable {
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickListener clickLinster;
    private List<ActivityInviteBean> activityInviteBeanList = new ArrayList<>();
    private List<RecyclerViewFilterBean<ActivityInviteBean>> filterList = new ArrayList<RecyclerViewFilterBean<ActivityInviteBean>>();
    private AvatarHelper avatarHelper = new AvatarHelper();
    public EventAddParticipantRecyclerViewAdapter(Context context,List<ActivityInviteBean> activityInviteBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityInviteBeanList = activityInviteBeanList;
        for (int i = 0; i < activityInviteBeanList.size(); i++) {
            RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = new RecyclerViewFilterBean();
            recyclerViewFilterBean.setPosition(i);
            recyclerViewFilterBean.setData(activityInviteBeanList.get(i));
            filterList.add(recyclerViewFilterBean);
        }
    }

    @NonNull
    @Override
    public EventAddParticipantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_event_add_participant, parent,false);
        return new EventAddParticipantRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = filterList.get(position);
        System.out.println("recyclerViewFilterBean.getData().getUserName() = " + recyclerViewFilterBean.getData().getUserName());
        ActivityInviteBean activityInviteBean = recyclerViewFilterBean.getData();
        System.out.println("activityInviteBean.getUserName() = " + activityInviteBean.getUserName());
        holder.bindInformation(activityInviteBean.getUserName(),activityInviteBean.getAvatar(),activityInviteBean.isInvite());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public ActivityInviteBean getActivityInviteBean(int position){
        return filterList.get(position).getData();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView friends_photo;
        TextView friends_name;
        CheckBox invite;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            friends_photo = itemView.findViewById(R.id.friends_photo);
            friends_name = itemView.findViewById(R.id.friends_name);
            invite = itemView.findViewById(R.id.invite);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String userName, String avatar,boolean isInvite){
            friends_photo.setImageBitmap(avatarHelper.getImageResource(avatar));
            friends_name.setText(userName);
            invite.setChecked(isInvite);
        }

        @Override
        public void onClick(View v) {
            if(clickLinster != null){
                clickLinster.onClick(v,getAdapterPosition());
            }
        }
    }
    public void  setClickListener(ClickListener clickLinster){
        this.clickLinster = clickLinster;
    }
    public void dataInsert(ActivityInviteBean activityInviteBean){
        RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
        recyclerViewFilterBean.setPosition(activityInviteBeanList.size());
        recyclerViewFilterBean.setData(activityInviteBean);
        filterList.add(recyclerViewFilterBean);
        activityInviteBeanList.add(activityInviteBean);
        notifyItemInserted(getItemCount());
    }
    public void dataUpdate(ActivityInviteBean activityInviteBean,int position){
        RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = filterList.get(position);

        ActivityInviteBean recyclerViewFilterBeanData = recyclerViewFilterBean.getData();
        recyclerViewFilterBeanData.setInvite(activityInviteBean.isInvite());
        notifyItemChanged(position);
        position = recyclerViewFilterBean.getPosition();
        activityInviteBeanList.get(position).setInvite(activityInviteBean.isInvite());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            //執行過濾操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString();
                System.out.println("filterString = " + filterString);
                if (filterString.isEmpty()) {
                    List<RecyclerViewFilterBean<ActivityInviteBean>> filterInviteList = new ArrayList<>();
                    //沒有過濾的內容
                    for (int i = 0; i < activityInviteBeanList.size(); i++) {
                        RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                        recyclerViewFilterBean.setPosition(i);
                        recyclerViewFilterBean.setData(activityInviteBeanList.get(i));
                        filterInviteList.add(recyclerViewFilterBean);
                    }
                    filterList = filterInviteList;
                } else {
                    List<RecyclerViewFilterBean<ActivityInviteBean>> filterInviteList = new ArrayList<>();
                    for (int i = 0; i < activityInviteBeanList.size(); i++) {
                        ActivityInviteBean activityInviteBean = activityInviteBeanList.get(i);
                        String userName = activityInviteBean.getUserName();
                        System.out.println("userName = " + userName);
                        //根據需求，新增過濾內容
                        if (userName.contains(filterString)) {
                            RecyclerViewFilterBean<ActivityInviteBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                            recyclerViewFilterBean.setData(activityInviteBean);
                            recyclerViewFilterBean.setPosition(i);
                            filterInviteList.add(recyclerViewFilterBean);
                        }
                    }

                    filterList = filterInviteList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }
            //把過濾後的值返回出來
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<RecyclerViewFilterBean<ActivityInviteBean>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }

}
