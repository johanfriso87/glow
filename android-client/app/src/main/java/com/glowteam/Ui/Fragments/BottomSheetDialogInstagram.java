package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.glowteam.R;
import com.glowteam.Ui.Activity.ShareActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
public class BottomSheetDialogInstagram extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_instagram,
                container, false);

        LinearLayout algo_button = v.findViewById(R.id.instagram_story);
        LinearLayout course_button = v.findViewById(R.id.instagram_feed);

        algo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getActivity(),
//                        "Algorithm Shared", Toast.LENGTH_SHORT)
//                        .show();
                ((ShareActivity)getActivity()).customeSharingToStory();
                dismiss();
            }
        });

        course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Toast.makeText(getActivity(),
//                        "Course Shared", Toast.LENGTH_SHORT)
//                        .show();
                ((ShareActivity)getActivity()).customeSharingToFeed();
                dismiss();
            }
        });
        return v;
    }
}
