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
import tw.com.businessmeet.bean.FriendGroupBean;
import tw.com.businessmeet.bean.RecyclerViewFilterBean;
import tw.com.businessmeet.helper.AvatarHelper;


public class FriendGroupRecyclerViewAdapter extends RecyclerView.Adapter<FriendGroupRecyclerViewAdapter.ViewHolder> implements Filterable {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<FriendGroupBean> friendGroupBeanList;
    private ClickListener clickLinster;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private List<RecyclerViewFilterBean<FriendGroupBean>> filterList = new ArrayList<RecyclerViewFilterBean<FriendGroupBean>>();

    public FriendGroupRecyclerViewAdapter(Context context, List<FriendGroupBean> friendGroupBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendGroupBeanList = friendGroupBeanList;
        for (int i = 0; i < friendGroupBeanList.size(); i++) {
            RecyclerViewFilterBean<FriendGroupBean> filterBean = new RecyclerViewFilterBean<>();
            filterBean.setPosition(i);
            filterBean.setData(friendGroupBeanList.get(i));
            filterList.add(filterBean);
        }
    }

    @NonNull
    @Override
    public FriendGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friend_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendGroupRecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewFilterBean<FriendGroupBean> filterBean = filterList.get(position);
        FriendGroupBean friendGroupBean = filterBean.getData();
        holder.bindInformation(friendGroupBean.getGroupName(), friendGroupBean.getCount().toString());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
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

        void bindInformation(String name, String count) {
            groupName.setText(name);
            groupCount.setText(count);
        }

        @Override
        public void onClick(View v) {
            if (clickLinster != null) {
                clickLinster.onClick(v, getAdapterPosition());
            }
        }

    }

    public FriendGroupBean getFriendGroupBean(int position) {
        return filterList.get(position).getData();
    }

    public void setClickListener(ClickListener clickLinster) {
        this.clickLinster = clickLinster;
    }

    public void dataInsert(FriendGroupBean friendGroupBean) {
        RecyclerViewFilterBean<FriendGroupBean> filterBean = new RecyclerViewFilterBean<>();
        filterBean.setData(friendGroupBean);
        filterBean.setPosition(friendGroupBeanList.size());
        filterList.add(filterBean);
        friendGroupBeanList.add(friendGroupBean);
        notifyItemInserted(getItemCount());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            //執行過濾操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString();
                if (filterString.isEmpty()) {
                    List<RecyclerViewFilterBean<FriendGroupBean>> filterGroupList = new ArrayList<>();
                    //沒有過濾的內容
                    for (int i = 0; i < friendGroupBeanList.size(); i++) {
                        RecyclerViewFilterBean<FriendGroupBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                        recyclerViewFilterBean.setPosition(i);
                        recyclerViewFilterBean.setData(friendGroupBeanList.get(i));
                        filterGroupList.add(recyclerViewFilterBean);
                    }
                    filterList = filterGroupList;
                } else {
                    List<RecyclerViewFilterBean<FriendGroupBean>> filterInviteList = new ArrayList<>();
                    for (int i = 0; i < friendGroupBeanList.size(); i++) {
                        FriendGroupBean friendGroupBean = friendGroupBeanList.get(i);
                        String groupName = friendGroupBean.getGroupName();
                        //根據需求，新增過濾內容
                        if (groupName.contains(filterString) || groupName.toLowerCase().contains(filterString.toLowerCase()) || groupName.toUpperCase().contains(filterString.toUpperCase())) {
                            RecyclerViewFilterBean<FriendGroupBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                            recyclerViewFilterBean.setData(friendGroupBean);
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
                filterList = (ArrayList<RecyclerViewFilterBean<FriendGroupBean>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

}