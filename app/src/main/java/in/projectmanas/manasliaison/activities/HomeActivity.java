package in.projectmanas.manasliaison.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.RecruitmentDetails;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.backendless_classes.UserTable;
import in.projectmanas.manasliaison.constants.ConstantsManas;
import in.projectmanas.manasliaison.listeners.DetailsUpdatedListener;
import in.projectmanas.manasliaison.tasks.UpdateAllDetails;
import in.projectmanas.manasliaison.tasks.UpdateSchedule;

public class HomeActivity extends AppCompatActivity
        implements DetailsUpdatedListener {

    private int phase, size;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNumberApplicants, tvNumberInterviewConducted, tvNumTPShortlisted, tvNumSelected;
    private String regNumber, userName, emailID;
    private int nInterviewConducted, nTPShortlisted, nApplicants, nSelected;
    private int prevInterviewConducted, prevTPShortlisted, prevApplicants, prevSelected;
    private String deviceToken;
    private String reScheduleCall;
    private String onlineChallengeDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private ImageView imageButtonEditNavHeader;
    private TextView tvNavHeaderName, tvNavHeaderEmailID, tvNavHeaderRegNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        try {
            if (getIntent().getStringExtra("notifySection") != null) {
                new UpdateSchedule(this, getIntent().getStringExtra("notifySection")).execute();
                getIntent().removeExtra("notifySection");
            }
        } catch (Exception e) {
        }
        try {
            if (getIntent().getBooleanExtra("fromLogin", false)) {
                swipeRefreshLayout.setRefreshing(true);
                getIntent().removeExtra("fromLogin");
            } else {
                if (emailID.equals("emailID")) {
                    logout();
                }
            }
        } catch (Exception e) {
        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("name", "name");
        emailID = sharedPreferences.getString("emailID", "emailID");

        //Snackbar.make(coordinatorLayout, "Loading data please wait", Snackbar.LENGTH_INDEFINITE).show();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
        if (getSharedPreferences("UserDetails", Context.MODE_PRIVATE).getString("regNumber", null) == null) {
            linearLayout.setVisibility(View.INVISIBLE);
        } else onNewDetailsFetched();
        getBackendlessDeviceToken();
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        getData();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    private void fillUserTable() {
        String whereClause = "registrationNumber = " + regNumber;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                if (response.size() > 0) {
                    UserTable userTable = response.get(0);
                    userTable.setDeviceToken(deviceToken);
                    userTable.saveAsync(new AsyncCallback<UserTable>() {
                        @Override
                        public void handleResponse(UserTable response) {

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    UserTable userTable = new UserTable();
                    userTable.setRegistrationNumber(regNumber);
                    userTable.setDeviceToken(deviceToken);
                    userTable.saveAsync(new AsyncCallback<UserTable>() {
                        @Override
                        public void handleResponse(UserTable response) {
                            Snackbar.make(coordinatorLayout, "User added", Snackbar.LENGTH_LONG);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                UserTable userTable = new UserTable();
                userTable.setRegistrationNumber(regNumber);
                userTable.setDeviceToken(deviceToken);
                userTable.saveAsync(new AsyncCallback<UserTable>() {
                    @Override
                    public void handleResponse(UserTable response) {
                        Snackbar.make(coordinatorLayout, "User added", Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

    }

    private void getAndCacheRecruitmentDetails() {
        RecruitmentDetails.findFirstAsync(new AsyncCallback<RecruitmentDetails>() {
            @Override
            public void handleResponse(RecruitmentDetails response) {
                phase = response.getPhase();
                reScheduleCall = response.getReScheduleCall();
                onlineChallengeDate = response.getOnlineChallengeDate().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString("reScheduleCall", reScheduleCall)
                        .putString("onlineChallengeDate", onlineChallengeDate)
                        .apply();
                setCardStatus(phase);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, "Please connect to internet", Snackbar.LENGTH_INDEFINITE).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getBackendlessDeviceToken() {
        Backendless.Messaging.registerDevice(ConstantsManas.gcmId);
        Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
            @Override
            public void handleResponse(DeviceRegistration response) {
                deviceToken = response.getDeviceId();
                Log.d("token", deviceToken);
                if (deviceToken != null && regNumber != null) fillUserTable();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setCardStatus(int phase) {
        Log.d("Phase ", phase + "");
        //TODO : Make related changes in the view for the corresponding phase.
        //Snackbar.make(coordinatorLayout, "Phase " + phase, Snackbar.LENGTH_LONG).show();
    }

    private void linkViews() {
        setContentView(R.layout.app_bar_main);
        linearLayout = (LinearLayout) findViewById(R.id.ll_home);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_home);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_home);
        tvNumberApplicants = (TextView) findViewById(R.id.tv_stat_appl);
        tvNumberInterviewConducted = (TextView) findViewById(R.id.tv_stat_interview_conducted);
//        tvNumSelected = (TextView) findViewById(R.id.tv_stat_selected);
        tvNumTPShortlisted = (TextView) findViewById(R.id.tv_stat_tp_sl);
        imageButtonEditNavHeader = (ImageView) findViewById(R.id.nav_edit_profile);
        tvNavHeaderName = (TextView) findViewById(R.id.tv_nav_header_name);
        tvNavHeaderEmailID = (TextView) findViewById(R.id.tv_nav_header_email_id);
        tvNavHeaderRegNumber = (TextView) findViewById(R.id.tv_nav_header_reg_number);
        imageButtonEditNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
    }

    private void getData() {
        ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
            @Override
            public void handleResponse(Sheet response) {
                final String[] params = new String[]{response.getEmailID(), response.getInterviewStatus1(), response.getInterviewStatus2(), response.getTpStatus(), response.getName(), response.getRegNumber(), response.getMobileNumber(), response.getPrefDiv1(), response.getPrefDiv2(), response.getPref1Schedule(), response.getPref2Schedule()};
                /*
                ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(LoginActivity.mCredential, HomeActivity.this);
                readSpreadSheet.delegate = HomeActivity.this;
                readSpreadSheet.execute(params);
                */
                UpdateAllDetails updateAllDetails = new UpdateAllDetails(HomeActivity.this);
                updateAllDetails.delegate = HomeActivity.this;
                updateAllDetails.execute(params);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, "Please connect to internet", Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onDetailsUpdated() {
        onNewDetailsFetched();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onUpdationFailed() {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(coordinatorLayout, "Please connect to Internet", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void onNewDetailsFetched() {
        getAllCacheData();
        if (deviceToken != null && regNumber != null) fillUserTable();
        setDataToViews();
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void setDataToViews() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new DecelerateInterpolator(.999f));
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                tvNumberApplicants.setText(String.valueOf((int) ((1 - fraction) * prevApplicants + fraction * nApplicants)));
                tvNumberInterviewConducted.setText(String.valueOf((int) ((1 - fraction) * prevInterviewConducted + fraction * nInterviewConducted)));
                tvNumTPShortlisted.setText(String.valueOf((int) ((1 - fraction) * prevTPShortlisted + fraction * nTPShortlisted)));
//                tvNumSelected.setText((int) (fraction * nSelected));
            }
        });
        animator.setStartDelay(500);
        animator.start();
        tvNavHeaderEmailID.setText(emailID);
        tvNavHeaderName.setText(userName);
        tvNavHeaderRegNumber.setText(regNumber);
    }


    int getStat(String prefData) {
        try {
            return Integer.parseInt(prefData);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void getAllCacheData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("name", "name");
        emailID = sharedPreferences.getString("emailID", "emailID");
        regNumber = sharedPreferences.getString("regNumber", "regNumber");
        /*
        interviewStatus1 = sharedPreferences.getString("interviewStatus1", "interviewStatus1");
        interviewStatus2 = sharedPreferences.getString("interviewStatus2", "interviewStatus2");
        tpStatus = sharedPreferences.getString("tpStatus", "tpStatus");
        mobileNumber = sharedPreferences.getString("mobileNumber", "mobileNumber");
        prefDiv1 = sharedPreferences.getString("prefDiv1", "prefDiv1");
        prefDiv2 = sharedPreferences.getString("prefDiv2", "prefDiv2");
        pref1Schedule = sharedPreferences.getString("pref1Schedule", "pref1Schedule");
        pref2Schedule = sharedPreferences.getString("pref2Schedule", "pref2Schedule");
        */
        prevApplicants = nApplicants;
        nApplicants = getStat(sharedPreferences.getString("numApplicants", "numApplicants"));
        prevInterviewConducted = nInterviewConducted;
        nInterviewConducted = getStat(sharedPreferences.getString("numInterviewConducted", "numInterviewConducted"));
        prevTPShortlisted = nTPShortlisted;
        nTPShortlisted = getStat(sharedPreferences.getString("numTPShortlisted", "numTPShortlisted"));
        prevSelected = nSelected;
        nSelected = getStat(sharedPreferences.getString("numSelected", "numSelected"));

        getAndCacheRecruitmentDetails();
    }

    public void onHomeItemSelected(View view) {
        // Handle navigation view item clicks here.
        int id = view.getId();

        //TODO:
        switch (id) {
            case R.id.nav_home:
                refresh();
                break;
            case R.id.nav_interview:
                startActivity(new Intent(HomeActivity.this, InterviewActivity.class));
                break;
            case R.id.nav_online_challenge:
                if (phase == 0)
                    startActivity(new Intent(HomeActivity.this, OnlineChallengeCommingSoon.class));
                else if (phase == 1)
                    startActivity(new Intent(HomeActivity.this, OnlineChallengeOngoing.class));
                else {
                    Snackbar.make(coordinatorLayout, "Online Challenge is over", Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.nav_orientation:
                startActivity(new Intent(HomeActivity.this, OrientationActivity.class));
                break;
            case R.id.nav_task_phase:
                startActivity(new Intent(HomeActivity.this, TaskPhaseActivity.class));
                break;
            case R.id.nav_edit_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.ib_logout:
                openLogoutAlert();
                break;
            case R.id.nav_about:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
            case R.id.nav_support:
                startActivity(new Intent(HomeActivity.this, SupportActivity.class));
                break;
        }
    }

    private void openLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("Logout")
                .setIcon(R.drawable.logout)
                .setMessage("Goodbye " + userName + ". Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();

    }
}
