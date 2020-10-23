package tw.com.businessmeet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;


public class MatchedDeviceRecyclerViewAdapter extends RecyclerView.Adapter<MatchedDeviceRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<UserInformationBean> userInformationBeanList;
    private SearchClickListener searchClickListener;
    public MatchedDeviceRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }
    private UserInformationBean ufb;

    @NonNull
    @Override
    public MatchedDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_matched_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchedDeviceRecyclerViewAdapter.ViewHolder holder, int position) {
        ufb = userInformationBeanList.get(position);
        AvatarHelper avatarHelper = new AvatarHelper();
        Bitmap avatar = avatarHelper.getImageResource(ufb.getAvatar());
        holder.bindInformation(ufb.getName(), avatar);
    }

    @Override
    public int getItemCount() {
        return userInformationBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView search_pro_pic_small;
        TextView search_name;



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_pro_pic_small = itemView.findViewById(R.id.search_pro_pic_small);
            search_name = itemView.findViewById(R.id.search_name);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String userName, Bitmap avatar){
            search_name.setText(userName);
            search_pro_pic_small.setImageBitmap(avatar);
        }

        @Override
        public void onClick(View v) {
            if(searchClickListener != null){
                searchClickListener.onSearchClick(v,getAdapterPosition());
            }
        }

    }
    public UserInformationBean getUserInformation(int position){
        return userInformationBeanList.get(position);
    }
    public void  setClickListener(SearchClickListener searchClickLinster){
        this.searchClickListener = searchClickLinster;
    }
    public void dataInsert(UserInformationBean userInformationBean){
        Log.d("resultDataInsert",userInformationBean.getIdentifier());
        userInformationBeanList.add(userInformationBean);
        notifyItemInserted(getItemCount());
    }
    public interface SearchClickListener{
        void onSearchClick(View view, int position);
    }

}