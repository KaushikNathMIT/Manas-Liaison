package in.projectmanas.manasliaison.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.activities.CCPLActivity;
import in.projectmanas.manasliaison.activities.GithubActivity;
import in.projectmanas.manasliaison.activities.UploadCVActivity;
import in.projectmanas.manasliaison.backendless_classes.UserTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterviewScheduledFragment extends Fragment {

    private ImageButton directionButton;
    private Button confirmSchedule, reSchedule;
    private TextView uploadCV;
    private TextView compCodeProfileLink;
    private TextView githubID;
    private ImageView tickUCV, tickCCPL, tickGID;
    private UserTable userTable;


    public InterviewScheduledFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_interview_scheduled, container, false);
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String whereClause = "registrationNumber = " + sharedPreferences.getString("regNumber", "regNumber");
        queryBuilder.setWhereClause(whereClause);
        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                userTable = response.get(0);
                linkViews(view);
                setListeners();
                checkData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        return view;
    }

    private void checkData() {
        if (userTable.getCV().contains("api.backendless.com")) {
            tickUCV.setVisibility(View.VISIBLE);
        }
        if (userTable.getHackerRankID() != null && userTable.getHackerRankID().length() > 1) {
            tickCCPL.setVisibility(View.VISIBLE);
        }
        if (userTable.getGithubID() != null && userTable.getGithubID().length() > 1) {
            tickGID.setVisibility(View.VISIBLE);
        }
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
                startActivityForResult(new Intent(getContext(), UploadCVActivity.class), 006);
            }
        });
        compCodeProfileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), CCPLActivity.class), 005);
            }
        });
        githubID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), GithubActivity.class), 004);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 006) {
            if (resultCode != 3) {
                tickUCV.setVisibility(View.VISIBLE);
                final String cvURl = data.getStringExtra("urlCV");
                userTable.setCV(cvURl);
                userTable.saveAsync(new AsyncCallback<UserTable>() {
                    @Override
                    public void handleResponse(UserTable response) {
                        Log.d("status", "CV uploaded successfully");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            } else {
                Toast.makeText(getContext(), data.getStringExtra("statusCV"), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 005) {
            userTable.setHackerRankID(data.getStringExtra("ccpl"));
            userTable.saveAsync(new AsyncCallback<UserTable>() {
                @Override
                public void handleResponse(UserTable response) {
                    Log.d("status", "Hackerrank ID uploaded successfully");
                    tickCCPL.setVisibility(View.VISIBLE);
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        } else if (requestCode == 004) {
            userTable.setGithubID(data.getStringExtra("gid"));
            userTable.saveAsync(new AsyncCallback<UserTable>() {
                @Override
                public void handleResponse(UserTable response) {
                    Log.d("status", "Github ID uploaded successfully");
                    tickGID.setVisibility(View.VISIBLE);
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
    }

    private void linkViews(View view) {
        directionButton = view.findViewById(R.id.ib_direction);
        confirmSchedule = view.findViewById(R.id.button_confirm_schedule);
        reSchedule = view.findViewById(R.id.button_request_reschedule);
        uploadCV = view.findViewById(R.id.tv_upload_cv);
        compCodeProfileLink = view.findViewById(R.id.tv_competitive_coding_profile_link);
        githubID = view.findViewById(R.id.tv_github_id);
        tickUCV = view.findViewById(R.id.iv_tick_ucv);
        tickCCPL = view.findViewById(R.id.iv_tick_ccpl);
        tickGID = view.findViewById(R.id.iv_gid);
        tickUCV.setVisibility(View.INVISIBLE);
        tickCCPL.setVisibility(View.INVISIBLE);
        tickGID.setVisibility(View.INVISIBLE);
    }

}
