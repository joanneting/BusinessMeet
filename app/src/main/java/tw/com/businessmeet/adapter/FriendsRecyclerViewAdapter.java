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


public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private  List<UserInformationBean> userInformationBeanList;
    private ClickListener clickLinster;
    private AvatarHelper avatarHelper = new AvatarHelper();
    public FriendsRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }

    @NonNull
    @Override
    public FriendsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRecyclerViewAdapter.ViewHolder holder, int position) {
        UserInformationBean ufb = userInformationBeanList.get(position);
        holder.bindInformation(ufb.getName(),ufb.getAvatar(),ufb.getProfession());
    }

    @Override
    public int getItemCount() {
        return userInformationBeanList.size();
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
        return userInformationBeanList.get(position);
    }
    public void  setClickListener(ClickListener clickLinster){
        this.clickLinster = clickLinster;
    }
    public void dataInsert(UserInformationBean userInformationBean){
        userInformationBeanList.add(userInformationBean);
        notifyItemInserted(getItemCount());
    }
    public interface ClickListener{
        void onClick(View view, int position);
    }

}