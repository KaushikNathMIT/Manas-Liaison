package in.projectmanas.manasliaison.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.UserTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterviewSelectedFragment extends Fragment {
    private Button accept, reject;
    private Context context;
    private String status1;
    private String status2;
    private int divIndex;
    private SharedPreferences sharedPreferences;
    private TextView tvLabelInterviewSelected;
    private List<UserTable> response;
    private View ivConfetti;
    private ProgressBar progressBar;

    public InterviewSelectedFragment() {
        // Required empty public constructor
    }

    public void setDetails(Context context, String status1, String status2, int divIndex) {
        this.context = context;
        this.status1 = status1;
        this.status2 = status2;
        this.divIndex = divIndex;

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String whereClause = "registrationNumber = " + sharedPreferences.getString("regNumber", "regNumber");
        queryBuilder.setWhereClause(whereClause);

        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                InterviewSelectedFragment.this.response = response;

                if (isVisible())
                    updateViews();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void updateViews() {
        ivConfetti.setVisibility(View.VISIBLE);

        if (response.get(0).getDiv1().equals("UNSET") && InterviewSelectedFragment.this.divIndex == 1) {
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            tvLabelInterviewSelected.setVisibility(View.VISIBLE);
        } else if (response.get(0).getDiv2().equals("UNSET") && InterviewSelectedFragment.this.divIndex == 2) {
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            tvLabelInterviewSelected.setVisibility(View.VISIBLE);
        } else {
            tvLabelInterviewSelected.setText("We have already recorded your response.\nWe'll see you there!");
            tvLabelInterviewSelected.setVisibility(View.VISIBLE);
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_interview_selected, container, false);
        accept = view.findViewById(R.id.button_selected_accept);
        reject = view.findViewById(R.id.button_selected_reject);
        ivConfetti = view.findViewById(R.id.iv_confetti);
        tvLabelInterviewSelected = view.findViewById(R.id.tv_label_interview_selected);
        progressBar = view.findViewById(R.id.pb_interview_selected);
        progressBar.setVisibility(View.INVISIBLE);
        tvLabelInterviewSelected.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);
        reject.setVisibility(View.INVISIBLE);
        setListeners();

        if (response != null)
            updateViews();

        return view;
    }

    private void setListeners() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                accept.setClickable(false);
                reject.setClickable(false);
                if (status1.equals("ACCEPTED") && status2.equals("ACCEPTED")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning");
                    builder.setMessage("You have been selected for both your preferences. So, if you accept for both, we will decide your division");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveAcceptance(true);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    saveAcceptance(true);
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                accept.setClickable(false);
                reject.setClickable(false);
                saveAcceptance(false);
            }
        });
    }

    private void saveAcceptance(final boolean option) {
        String whereClause = "registrationNumber = " + sharedPreferences.getString("regNumber", "regNumber");
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                Log.d("received", response.get(0).getRegistrationNumber());
                if (divIndex == 1) response.get(0).setDiv1(option ? "TRUE" : "FALSE");
                else response.get(0).setDiv2(option ? "TRUE" : "FALSE");
                response.get(0).saveAsync(new AsyncCallback<UserTable>() {
                    @Override
                    public void handleResponse(UserTable response) {
                        Toast.makeText(context.getApplicationContext(), "Your response has been saved", Toast.LENGTH_LONG).show();
                        tvLabelInterviewSelected.setText("We have already recorded your response");
                        accept.setVisibility(View.INVISIBLE);
                        reject.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        accept.setClickable(true);
                        reject.setClickable(true);
                        Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                accept.setClickable(true);
                reject.setClickable(true);
                Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
