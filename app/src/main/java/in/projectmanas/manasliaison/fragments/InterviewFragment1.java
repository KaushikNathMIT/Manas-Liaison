package in.projectmanas.manasliaison.fragments;


import android.content.Context;
import android.content.SharedPreferences;
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
public class InterviewFragment1 extends Fragment {
    private TextView textViewStatus, textViewStatusDesc;

    public InterviewFragment1() {
        // Required empty public constructor
    }

    public static InterviewFragment1 getInstance() {
        return new InterviewFragment1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview1, container, false);
        textViewStatus = view.findViewById(R.id.tv_iv1_status);
        textViewStatusDesc = view.findViewById(R.id.tv_iv1_status_desc);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("interviewStatus1", "interviewStatus1");
        textViewStatus.setText(status);
        if (status.equals("ACCEPTED")) {
            textViewStatusDesc.setText("Congratulations, you’ve been selected for the task phase of this division. We are awaiting your response to our offer.");
        } else if (status.equals("REJECTED")) {
            textViewStatusDesc.setText("We regret to inform you that you have not been selected for the task phase round.");
        } else if (status.equals("RESULT PENDING")) {
            textViewStatusDesc.setText("We’ve completed your interview. The result of your interview will be announced, once all interviews have been completed.");
        } else if (status.equals("PENDING")) {
            textViewStatusDesc.setText("Our bots are busy at work scheduling your interview, take it easy on them, they aren’t human");
        }
        return view;
    }
}
