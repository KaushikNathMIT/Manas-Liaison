package in.projectmanas.manasliaison.fragments;


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
public class InterviewRejectedFragment extends Fragment {
    private TextView textViewStatus, textViewStatusDesc;
    private int index;

    public InterviewRejectedFragment() {
        // Required empty public constructor
    }

    public static InterviewRejectedFragment getInstance() {
        return new InterviewRejectedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview_rejected, container, false);
        return view;
    }
}
