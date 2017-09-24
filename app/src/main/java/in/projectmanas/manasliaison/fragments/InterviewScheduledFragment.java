package in.projectmanas.manasliaison.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.activities.UploadCVActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterviewScheduledFragment extends Fragment {

    private ImageButton directionButton;
    private Button confirmSchedule, reSchedule;
    private TextView uploadCV;
    private TextView compCodeProfileLink;
    private TextView githubID;


    public InterviewScheduledFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interview_scheduled, container, false);
        linkViews(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.com/maps/search/?api=1&query=ProjectManasManipal";
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(url));
                startActivity(httpIntent);
            }
        });
        uploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UploadCVActivity.class));
            }
        });
    }

    private void linkViews(View view) {
        directionButton = view.findViewById(R.id.ib_direction);
        confirmSchedule = view.findViewById(R.id.button_confirm_schedule);
        reSchedule = view.findViewById(R.id.button_request_reschedule);
        uploadCV = view.findViewById(R.id.tv_upload_cv);
        compCodeProfileLink = view.findViewById(R.id.tv_competitive_coding_profile_link);
        githubID = view.findViewById(R.id.tv_github_id);
    }

}
