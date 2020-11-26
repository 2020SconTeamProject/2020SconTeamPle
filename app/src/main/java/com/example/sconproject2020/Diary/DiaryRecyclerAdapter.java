package com.example.sconproject2020.Diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sconproject2020.Calendar.CalendarRecyclerAdapter;
import com.example.sconproject2020.R;

import java.util.ArrayList;

public class DiaryRecyclerAdapter  extends RecyclerView.Adapter<DiaryRecyclerAdapter.ViewHolder> {

    private ArrayList<DiaryRecyclerItem> mDate = new ArrayList<>();

    public DiaryRecyclerAdapter(ArrayList<DiaryRecyclerItem> mDate) {
        this.mDate = mDate;
    }

    private CalendarRecyclerAdapter.OnItemClickListener mListener = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }
    public void setOnItemClickListener(CalendarRecyclerAdapter.OnItemClickListener mListener){
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.diary_recycler_item, parent, false);
        DiaryRecyclerAdapter.ViewHolder vh = new DiaryRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryRecyclerItem item = mDate.get(position);
        holder.nameTv.setText(item.getTitle());
        holder.previewTv.setText(item.getPreviewText());
        holder.dateTv.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, previewTv, dateTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.diaryNameTv);
            previewTv = itemView.findViewById(R.id.diaryPreviewTv);
            dateTv = itemView.findViewById(R.id.diaryDateTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemLongClick(view, pos);
                        }
                    }
                    return  true;
                }
            });
        }
    }
}
