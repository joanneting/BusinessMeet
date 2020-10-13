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
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.helper.AvatarHelper;


public class UnmatchedDeviceRecyclerViewAdapter extends RecyclerView.Adapter<UnmatchedDeviceRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<UserInformationBean> userInformationBeanList;
    private MatchedClickListener matchedClickListener;
    private AvatarHelper avatarHelper = new AvatarHelper();
    public UnmatchedDeviceRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }

    @NonNull
    @Override
    public UnmatchedDeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_unmatched_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnmatchedDeviceRecyclerViewAdapter.ViewHolder holder, int position) {
        UserInformationBean ufb = userInformationBeanList.get(position);
        holder.bindInformation(ufb.getName(),ufb.getAvatar());

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

        void bindInformation(String userName, String avatar){
            search_name.setText(userName);
            search_pro_pic_small.setImageBitmap(avatarHelper.getImageResource(avatar));

        }

        @Override
        public void onClick(View v) {
            Log.d("results","unmatched1");
            if(matchedClickListener != null){
                Log.d("results","unmatched");
                matchedClickListener.onMatchedClick(v,getAdapterPosition());
            }
        }

    }
    public void  setClickListener(MatchedClickListener searchClickLinster){
        this.matchedClickListener = searchClickLinster;
    }
    public UserInformationBean getUserInformation(int position){
        return userInformationBeanList.get(position);
    }
    public void dataInsert(UserInformationBean userInformationBean){
        Log.d("resultDataInsert",userInformationBean.getIdentifier());
        userInformationBeanList.add(userInformationBean);
        notifyItemInserted(getItemCount());
    }
    public interface MatchedClickListener{
        void onMatchedClick(View view, int position);
    }

}