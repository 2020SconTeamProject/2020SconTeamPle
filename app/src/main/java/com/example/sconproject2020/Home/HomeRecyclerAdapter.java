package com.example.sconproject2020.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sconproject2020.Calendar.CalendarRecyclerAdapter;
import com.example.sconproject2020.R;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    ArrayList<HomeRecyclerItem> mData = new ArrayList<>();

    public HomeRecyclerAdapter(ArrayList<HomeRecyclerItem> mData) {
        this.mData = mData;
    }

    private CalendarRecyclerAdapter.OnCheckedChangedListener checkedChangedListener = null;

    public interface OnCheckedChangedListener{
        void onCheckedChanged(boolean isChecked, int position);
    }
    public void setOnCheckedChangedListener(CalendarRecyclerAdapter.OnCheckedChangedListener listener){
        this.checkedChangedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_recycler_item, parent, false);
        HomeRecyclerAdapter.ViewHolder vh = new HomeRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeRecyclerItem item = mData.get(position);
        holder.todoTextView.setText(item.getTodo());
        holder.whenTextView.setText(item.getWhen());
        holder.checkBox.setChecked(item.isChecked);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView todoTextView, whenTextView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            todoTextView = itemView.findViewById(R.id.home_todoTv);
            whenTextView = itemView.findViewById(R.id.home_whenTv);
            checkBox = itemView.findViewById(R.id.home_chkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = getAdapterPosition();
                    Log.e("pos",""+pos);

                    if(pos != RecyclerView.NO_POSITION){
                        if(checkedChangedListener != null){
                            checkedChangedListener.onCheckedChanged(b, pos);
                        }
                    }
                }
            });
        }
    }
}
