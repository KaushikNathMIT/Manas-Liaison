package in.projectmanas.manasliaison.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private String div;


    public InterviewSelectedFragment() {
        // Required empty public constructor
    }

    public void setDetails(Context context, String status1, String status2, String div) {

        this.context = context;
        this.status1 = status1;
        this.status2 = status2;
        this.div = div;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interview_selected, container, false);
        accept = view.findViewById(R.id.button_selected_accept);
        reject = view.findViewById(R.id.button_selected_reject);
        setListeners();
        return view;
    }

    private void setListeners() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status1.equals("ACCEPTED") && status2.equals("ACCEPTED")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                } else {
                    saveAcceptance();
                }
            }
        });
    }

    private void saveAcceptance() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String whereClause = "registrationNumber = " + sharedPreferences.getString("regNumber", "regNumber");
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                Log.d("received", response.get(0).getRegistrationNumber());
                response.get(0).setTaskPhaseDiv(div);
                response.get(0).saveAsync(new AsyncCallback<UserTable>() {
                    @Override
                    public void handleResponse(UserTable response) {
                        Toast.makeText(context.getApplicationContext(), "Your response has been saved", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}
