package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.businessmeet.FriendsTimelineActivity;
import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.helper.AvatarHelper;

public class FriendsTimelineRecyclerViewAdapter extends RecyclerView.Adapter<FriendsTimelineRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<TimelineBean> timelineBeanList;
    private ClickListener clickListener;
    public FriendsTimelineRecyclerViewAdapter(Context context, List<TimelineBean> timelineBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.timelineBeanList = timelineBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friends_timeline, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsTimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        //holder.friends_place.setText("八大");
        TimelineBean tlb = timelineBeanList.get(position);

        holder.bindInformation(tlb.getTimelinePropertiesNo()==1?tlb.getStartDate():tlb.getCreateDateStr(),tlb.getTitle());
    }

    @Override
    public int getItemCount() {
        return timelineBeanList.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friends_place;
        TextView friends_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friends_date = itemView.findViewById(R.id.friends_date);
            friends_place = itemView.findViewById(R.id.friends_place);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String date,String title){
            friends_date.setText(date);
            friends_place.setText(title);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onClick(view,getAdapterPosition());
            }
        }
    }
    public TimelineBean getTimelineBean(int position){
        return timelineBeanList.get(position);
    }
    public void  setClickListener(ClickListener clickListener){this.clickListener = clickListener;}
    public void dataInsert(TimelineBean timelineBean){
        timelineBeanList.add(timelineBean);
        notifyItemInserted(getItemCount());
    }
    public interface ClickListener{
        void onClick(View view, int position);
    }
}
