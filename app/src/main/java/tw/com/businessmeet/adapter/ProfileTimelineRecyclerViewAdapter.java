package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.UserInformationBean;

public class ProfileTimelineRecyclerViewAdapter extends RecyclerView.Adapter<ProfileTimelineRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<UserInformationBean> userInformationBeanList;
    private ClickListener clickListener;
    public ProfileTimelineRecyclerViewAdapter(Context context, List<UserInformationBean> userInformationBeanList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInformationBeanList = userInformationBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_profile_timeline, parent,false);
        return new ViewHolder(view);
    }

    //顯示的內容
    @Override
    public void onBindViewHolder(@NonNull ProfileTimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        //holder.profile_event_place.setText("八大");
        UserInformationBean ufb = userInformationBeanList.get(position);
        holder.bindInformation(ufb.getName(),ufb.getProfession());

    }

    @Override
    public int getItemCount() {
        return userInformationBeanList.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView profile_event_place;
        TextView profile_event_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_event_date = itemView.findViewById(R.id.profile_event_date);
            profile_event_place = itemView.findViewById(R.id.profile_event_place);
            itemView.setOnClickListener(this);
        }

        void bindInformation(String userName,String userCompany){
            profile_event_date.setText(userName);
            profile_event_place.setText(userCompany);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onClick(view,getAdapterPosition());
            }
        }
    }
    public UserInformationBean getUserInformation(int position){
        return userInformationBeanList.get(position);
    }
    public void  setClickListener(ClickListener clickListener){this.clickListener = clickListener;}
    public void dataInsert(UserInformationBean userInformationBean){
        userInformationBeanList.add(userInformationBean);
        notifyItemInserted(getItemCount());
    }
    public interface ClickListener{
        void onClick(View view, int position);
    }
}
