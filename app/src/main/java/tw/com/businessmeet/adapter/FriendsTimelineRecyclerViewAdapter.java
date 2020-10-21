package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.RecyclerViewFilterBean;
import tw.com.businessmeet.bean.TimelineBean;

public class FriendsTimelineRecyclerViewAdapter extends RecyclerView.Adapter<FriendsTimelineRecyclerViewAdapter.ViewHolder> implements Filterable {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<TimelineBean> timelineBeanList;
    private ClickListener clickListener;
    private List<RecyclerViewFilterBean<TimelineBean>> filterList = new ArrayList<>();

    public FriendsTimelineRecyclerViewAdapter(Context context, List<TimelineBean> timelineBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.timelineBeanList = timelineBeanList;
        for (int i = 0; i < timelineBeanList.size(); i++) {
            RecyclerViewFilterBean<TimelineBean> recyclerViewFilterBean = new RecyclerViewFilterBean();
            recyclerViewFilterBean.setPosition(i);
            recyclerViewFilterBean.setData(timelineBeanList.get(i));
            filterList.add(recyclerViewFilterBean);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friends_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsTimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        TimelineBean timelineBean = filterList.get(position).getData();

        holder.bindInformation(timelineBean.getTimelinePropertiesNo() == 1 ? timelineBean.getStartDate() : timelineBean.getCreateDateStr(), timelineBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friends_place;
        TextView friends_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friends_date = itemView.findViewById(R.id.friends_date);
            friends_place = itemView.findViewById(R.id.friends_place);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String date, String title) {
            friends_date.setText(date);
            friends_place.setText(title);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    public TimelineBean getTimelineBean(int position) {
        return filterList.get(position).getData();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void dataInsert(TimelineBean timelineBean) {
        RecyclerViewFilterBean<TimelineBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
        recyclerViewFilterBean.setData(timelineBean);
        recyclerViewFilterBean.setPosition(timelineBeanList.size());
        filterList.add(recyclerViewFilterBean);
        timelineBeanList.add(timelineBean);
        notifyItemInserted(getItemCount());
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
                    List<RecyclerViewFilterBean<TimelineBean>> filterInviteList = new ArrayList<>();
                    //沒有過濾的內容
                    for (int i = 0; i < timelineBeanList.size(); i++) {
                        RecyclerViewFilterBean<TimelineBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                        recyclerViewFilterBean.setPosition(i);
                        recyclerViewFilterBean.setData(timelineBeanList.get(i));
                        filterInviteList.add(recyclerViewFilterBean);
                    }
                    filterList = filterInviteList;
                } else {
                    List<RecyclerViewFilterBean<TimelineBean>> filterInviteList = new ArrayList<>();
                    for (int i = 0; i < timelineBeanList.size(); i++) {
                        TimelineBean timelineBean = timelineBeanList.get(i);
                        String title = timelineBean.getTitle();
                        //根據需求，新增過濾內容
                        if (title.contains(filterString) || title.toLowerCase().contains(filterString.toLowerCase()) || title.toUpperCase().contains(filterString.toUpperCase())) {
                            RecyclerViewFilterBean<TimelineBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                            recyclerViewFilterBean.setData(timelineBean);
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
                filterList = (ArrayList<RecyclerViewFilterBean<TimelineBean>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
