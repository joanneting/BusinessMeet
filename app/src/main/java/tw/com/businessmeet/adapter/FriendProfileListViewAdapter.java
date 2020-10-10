package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import tw.com.businessmeet.R;
import tw.com.businessmeet.bean.FriendCustomizationBean;

public class FriendProfileListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<FriendCustomizationBean> friendCustomizationBeanList;

    public FriendProfileListViewAdapter(Context context, List<FriendCustomizationBean> friendCustomizationBeanList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.friendCustomizationBeanList = friendCustomizationBeanList;
    }

    @Override
    public int getCount() {
        return friendCustomizationBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendCustomizationBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.recycler_view_row_friend_profile_memo, null);
        TextView title = (TextView) convertView.findViewById(R.id.friends_profile_information_memo_title);
        ChipGroup chips = (ChipGroup) convertView.findViewById(R.id.friends_profile_information_memo_content);

        title.setText(friendCustomizationBeanList.get(position).getName());
        String content = friendCustomizationBeanList.get(position).getContent();
        String[] chipContent = new String[friendCustomizationBeanList.get(position).getContent().length()];
        chipContent = content.split(",");
        for (int i = 1; i < chipContent.length; i++) {
            Chip chip = new Chip(context);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(chipContent[i]);
            chips.addView(chip);
        }

        return convertView;
    }
}