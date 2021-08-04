package com.glowteam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.R;
import com.glowteam.Ui.Activity.EditProfileActivity;

import static com.glowteam.Ui.Activity.EditProfileActivity.haircare;


public class HaircareAdapter extends RecyclerView.Adapter<HaircareAdapter.ViewHolder> {
    EditProfileActivity activity;
    LayoutInflater inflater;
    String[] hairColorName;
    int type;

    public HaircareAdapter(EditProfileActivity editProfileActivity, String[] hairColorName) {
        activity = editProfileActivity;
        this.hairColorName = hairColorName;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_skincare, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.care_name.setText(hairColorName[holder.getAdapterPosition()]);
        if (haircare.contains(hairColorName[holder.getAdapterPosition()])) {
            holder.select_care.setImageDrawable(activity.getResources().getDrawable(R.drawable.iv_selected));
        } else {
            holder.select_care.setImageDrawable(activity.getResources().getDrawable(R.drawable.iv_unselected));
        }
    }

    @Override
    public int getItemCount() {
        return hairColorName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView select_care;
        TextView care_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            select_care = (ImageView) itemView.findViewById(R.id.select_care);
            care_name = (TextView) itemView.findViewById(R.id.care_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (haircare.contains(hairColorName[getAdapterPosition()])) {
                        haircare.remove(hairColorName[getAdapterPosition()]);
                    } else {
                        haircare.add(hairColorName[getAdapterPosition()]);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}
