package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;

public class EventAddParticipantRecyclerViewAdapter extends RecyclerView.Adapter<EventAddParticipantRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickListener clickLinster;
    private List<ActivityInviteBean> activityInviteBeanList = new ArrayList<>();
    private AvatarHelper avatarHelper = new AvatarHelper();
    public EventAddParticipantRecyclerViewAdapter(Context context,List<ActivityInviteBean> activityInviteBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityInviteBeanList = activityInviteBeanList;

    }

    @NonNull
    @Override
    public EventAddParticipantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_event_add_participant, parent,false);
        return new EventAddParticipantRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityInviteBean activityInviteBean = activityInviteBeanList.get(position);
        holder.bindInformation(activityInviteBean.getUserName(),activityInviteBean.getAvatar(),activityInviteBean.isInvite());
    }

    @Override
    public int getItemCount() {
        return activityInviteBeanList.size();
    }

    public ActivityInviteBean getActivityInviteBean(int position){
        return activityInviteBeanList.get(position);
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
        activityInviteBeanList.add(activityInviteBean);
        notifyItemInserted(getItemCount());
    }
    public void dataUpdate(ActivityInviteBean activityInviteBean,int position){
        activityInviteBeanList.get(position).setInvite(activityInviteBean.isInvite());
        notifyItemChanged(position);
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }

}
