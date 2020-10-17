package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.ActivityInviteBean;

public class SelfInviteRecyclerViewAdapter extends RecyclerView.Adapter<SelfInviteRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
private List<ActivityInviteBean> activityInviteBeanList;
    public SelfInviteRecyclerViewAdapter(Context context, List<ActivityInviteBean> activityInviteBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityInviteBeanList = activityInviteBeanList;
    }

    @NonNull
    @Override
    public SelfInviteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_self_invite, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityInviteBean activityInviteBean = activityInviteBeanList.get(position);
        holder.bindInformation(activityInviteBean.getCreateDate(),activityInviteBean.getTitle(),activityInviteBean.getPlace(),activityInviteBean.getActivityDate());
    }

    @Override
    public int getItemCount() {
        return activityInviteBeanList.size();
    }

    public interface ClickListener {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView selfEventTitle;
        TextView selfEventPlace;
        TextView selfEventTime;
        TextView inviteDate;
        Button selfInviteAccept;
        Button selfInviteDecline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selfEventTitle = itemView.findViewById(R.id.self_event_title);
            selfEventPlace = itemView.findViewById(R.id.self_event_place);
            selfEventTime = itemView.findViewById(R.id.self_event_time);
            inviteDate = itemView.findViewById(R.id.invite_date);
            selfInviteAccept = itemView.findViewById(R.id.self_invite_accept);
            selfInviteDecline = itemView.findViewById(R.id.self_invite_Decline);
        }
        void  bindInformation(String createDate,String title,String place,String activityDate){
            selfEventTitle.setText(title);
            selfEventPlace.setText(place);
            inviteDate.setText(createDate);
            selfEventTime.setText(activityDate);
        }
    }
    public ActivityInviteBean activityInviteBean(int position){
        return activityInviteBeanList.get(position);
    }

    public void dataInsert(ActivityInviteBean activityInviteBean){
        activityInviteBeanList.add(activityInviteBean);
        notifyItemInserted(getItemCount());
    }
    public View.OnClickListener accept = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    public View.OnClickListener decline =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
