package tw.com.businessmeet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.activity.EventActivity;
import tw.com.businessmeet.activity.SelfIntroductionActivity;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.service.Impl.ActivityInviteServiceImpl;

public class SelfInviteRecyclerViewAdapter extends RecyclerView.Adapter<SelfInviteRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private ActivityInviteServiceImpl activityInviteService = new ActivityInviteServiceImpl();
    private List<ActivityInviteBean> activityInviteBeanList;

    public SelfInviteRecyclerViewAdapter(Context context, List<ActivityInviteBean> activityInviteBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityInviteBeanList = activityInviteBeanList;
    }

    @NonNull
    @Override
    public SelfInviteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_self_invite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityInviteBean activityInviteBean = activityInviteBeanList.get(position);
        holder.bindInformation(activityInviteBean.getCreateDate(), activityInviteBean.getTitle(), activityInviteBean.getPlace(),
                activityInviteBean.getActivityDate(), activityInviteBean.getActivityInviteNo(), activityInviteBean.getActivityNo());
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
        TextView inviteYear;
        TextView inviteDate;
        Button selfInviteAccept;
        Button selfInviteDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selfEventTitle = itemView.findViewById(R.id.self_event_title);
            selfEventPlace = itemView.findViewById(R.id.self_event_place);
            selfEventTime = itemView.findViewById(R.id.self_event_time);
            inviteYear = itemView.findViewById(R.id.invite_year);
            inviteDate = itemView.findViewById(R.id.invite_date);
            selfInviteAccept = itemView.findViewById(R.id.self_invite_accept);
            selfInviteDecline = itemView.findViewById(R.id.self_invite_Decline);
        }

        void bindInformation(String createDate, String title, String place, String activityDate, Integer inviteNo, Integer activityNo) {
            selfEventTitle.setText(title);
            selfEventPlace.setText(place);
            inviteDate.setText(createDate.replace("2020-", ""));
            inviteYear.setText(createDate.split("-")[0]);
            selfEventTime.setText(activityDate);
            selfInviteAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityInviteBean activityInviteBean = new ActivityInviteBean();
                    activityInviteBean.setActivityInviteNo(inviteNo);
                    activityInviteBean.setStatus(2);
                    AsyncTaskHelper.execute(() -> activityInviteService.update(activityInviteBean), resultBean -> {
                        Intent intent = new Intent();
                        intent.setClass(context, EventActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("timelineNo", activityNo.toString());
                        bundle.putString("page", "self");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    });

                }
            });
            selfInviteDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityInviteBean activityInviteBean = new ActivityInviteBean();
                    AsyncTaskHelper.execute(() -> activityInviteService.delete(inviteNo), resultBean -> {
                        Intent intent = new Intent();
                        intent.setClass(context, SelfIntroductionActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    });

                }
            });
        }
    }

    public ActivityInviteBean activityInviteBean(int position) {
        return activityInviteBeanList.get(position);
    }

    public void dataInsert(ActivityInviteBean activityInviteBean) {
        activityInviteBeanList.add(activityInviteBean);
        notifyItemInserted(getItemCount());
    }

}
