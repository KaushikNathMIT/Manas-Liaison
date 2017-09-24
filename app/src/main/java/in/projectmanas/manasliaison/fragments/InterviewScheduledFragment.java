package in.projectmanas.manasliaison.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.projectmanas.manasliaison.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterviewScheduledFragment extends Fragment {

    private TextView labelNav;

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
        labelNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.com/maps/search/?api=1&query=ProjectManasManipal";
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(url));
                startActivity(httpIntent);
            }
        });
    }

    private void linkViews(View view) {
        labelNav = view.findViewById(R.id.tv_label_nav_workshop);

    }

}
