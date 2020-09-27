package tw.com.businessmeet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tw.com.businessmeet.R;

public class EventAddParticipantRecyclerViewAdapter extends RecyclerView.Adapter<EventAddParticipantRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;

    public EventAddParticipantRecyclerViewAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public EventAddParticipantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_row_event_add_participant, parent,false);
        return new EventAddParticipantRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ClickListener {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
