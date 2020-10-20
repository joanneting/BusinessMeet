package tw.com.businessmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.bean.RecyclerViewFilterBean;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;


public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> implements Filterable {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<UserInformationBean> userInformationBeanList;
    private List<RecyclerViewFilterBean<UserInformationBean>> filterList = new ArrayList<RecyclerViewFilterBean<UserInformationBean>>();
    private ClickListener clickLinster;
    private AvatarHelper avatarHelper = new AvatarHelper();
    public FriendsRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
        for (int i = 0; i < userInformationBeanList.size(); i++) {
            RecyclerViewFilterBean<UserInformationBean> filterBean = new RecyclerViewFilterBean<>();
            filterBean.setPosition(i);
            filterBean.setData(userInformationBeanList.get(i));
        }
    }

    @NonNull
    @Override
    public FriendsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewFilterBean<UserInformationBean> filterBean = filterList.get(position);
        UserInformationBean ufb = filterBean.getData();
        holder.bindInformation(ufb.getName(),ufb.getAvatar(),ufb.getProfession());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView friends_photo;
        TextView friends_name;
        TextView friends_profession;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            friends_photo = itemView.findViewById(R.id.friends_photo);
            friends_name = itemView.findViewById(R.id.friends_name);
            friends_profession = itemView.findViewById(R.id.friends_profession);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String userName, String avatar,String userProfession){
            friends_photo.setImageBitmap(avatarHelper.getImageResource(avatar));
            friends_name.setText(userName);
            friends_profession.setText(userProfession);
        }

        @Override
        public void onClick(View v) {
            if(clickLinster != null){
                clickLinster.onClick(v,getAdapterPosition());
            }
        }

    }
    public UserInformationBean getUserInformation(int position){
        return filterList.get(position).getData();
    }
    public void  setClickListener(ClickListener clickLinster){
        this.clickLinster = clickLinster;
    }
    public void dataInsert(UserInformationBean userInformationBean){
        RecyclerViewFilterBean<UserInformationBean> filterBean = new RecyclerViewFilterBean<>();
        filterBean.setData(userInformationBean);
        filterBean.setPosition(userInformationBeanList.size());
        filterList.add(filterBean);
        userInformationBeanList.add(userInformationBean);
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
                    List<RecyclerViewFilterBean<UserInformationBean>> filterFriendList = new ArrayList<>();
                    //沒有過濾的內容
                    for (int i = 0; i < userInformationBeanList.size(); i++) {
                        RecyclerViewFilterBean<UserInformationBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                        recyclerViewFilterBean.setPosition(i);
                        recyclerViewFilterBean.setData(userInformationBeanList.get(i));
                        filterFriendList.add(recyclerViewFilterBean);
                    }
                    filterList = filterFriendList;
                } else {
                    List<RecyclerViewFilterBean<UserInformationBean>> filterInviteList = new ArrayList<>();
                    for (int i = 0; i < userInformationBeanList.size(); i++) {
                        UserInformationBean userInformationBean = userInformationBeanList.get(i);
                        String userName = userInformationBean.getName();
                        //根據需求，新增過濾內容
                        if (userName.contains(filterString) || userName.toLowerCase().contains(filterString.toLowerCase()) || userName.toUpperCase().contains(filterString.toUpperCase())) {
                            RecyclerViewFilterBean<UserInformationBean> recyclerViewFilterBean = new RecyclerViewFilterBean<>();
                            recyclerViewFilterBean.setData(userInformationBean);
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
                filterList = (ArrayList<RecyclerViewFilterBean<UserInformationBean>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }

}