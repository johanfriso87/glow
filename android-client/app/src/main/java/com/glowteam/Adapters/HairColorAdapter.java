package com.glowteam.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.R;
import com.glowteam.Ui.Activity.EditProfileActivity;

public class HairColorAdapter extends RecyclerView.Adapter<HairColorAdapter.ViewHolder> {
    EditProfileActivity activity;
    String[] hairColor;
    LayoutInflater inflater;
    String[] hairColorName;

    public HairColorAdapter(EditProfileActivity editProfileActivity, String[] hairColor, String[] hairColorName) {
        activity = editProfileActivity;
        this.hairColor = hairColor;
        this.hairColorName = hairColorName;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_hair_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.hair_color.setColorFilter(Color.parseColor(hairColor[holder.getAdapterPosition()]), android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.hair_color_name.setText(hairColorName[holder.getAdapterPosition()]);
    }

    @Override
    public int getItemCount() {
        return hairColorName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hair_color;
        TextView hair_color_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hair_color = (ImageView) itemView.findViewById(R.id.hair_color);
            hair_color_name = (TextView) itemView.findViewById(R.id.hair_color_name);
        }
    }
}
