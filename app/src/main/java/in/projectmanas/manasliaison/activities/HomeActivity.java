package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.RecruitmentDetails;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.backendless_classes.UserTable;
import in.projectmanas.manasliaison.constants.ConstantsManas;
import in.projectmanas.manasliaison.listeners.SheetDataFetchedListener;
import in.projectmanas.manasliaison.tasks.ReadSpreadSheet;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SheetDataFetchedListener {

    private int phase, size;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNumberApplicants, tvNumberInterviewConducted, tvNumTPShortlisted, tvNumSelected, tvNavHeaderName, tvNavHeaderEmailID, tvNavHeaderRegNumber;
    private String regNumber, userName, emailID, interviewStatus1, interviewStatus2, tpStatus, mobileNumber, prefDiv1, prefDiv2, pref1Schedule, pref2Schedule;
    private String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        emailID = LoginActivity.mCredential.getSelectedAccountName();
        getBackendlessDeviceToken();
        getRecruitmentPhase();
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        getData();
    }

    private void cacheData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor
                .putString("name", userName)
                .putString("emailID", emailID)
                .putString("interviewStatus1", interviewStatus1)
                .putString("interviewStatus2", interviewStatus2)
                .putString("tpStatus", tpStatus)
                .putString("regNumber", regNumber)
                .putString("mobileNumber", mobileNumber)
                .putString("deviceToken", deviceToken)
                .putString("prefDiv1", prefDiv1)
                .putString("prefDiv2", prefDiv2)
                .putString("pref1Schedule", pref1Schedule)
                .putString("pref2Schedule", pref2Schedule)
                .apply();
        //TODO: add to APP
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

            }
        });
    }

    private void getRecruitmentPhase() {
        RecruitmentDetails.findFirstAsync(new AsyncCallback<RecruitmentDetails>() {
            @Override
            public void handleResponse(RecruitmentDetails response) {
                phase = response.getPhase();
                setCardStatus(phase);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void getBackendlessDeviceToken() {
        Backendless.Messaging.registerDevice(ConstantsManas.gcmId);
        Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
            @Override
            public void handleResponse(DeviceRegistration response) {
                deviceToken = response.getDeviceToken();
                Log.d("token", deviceToken);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void setCardStatus(int phase) {
        Log.d("Phase ", phase + "");
        //TODO : Make related changes in the view for the corresponding phase.
        Snackbar.make(coordinatorLayout, "Phase " + phase, Snackbar.LENGTH_LONG).show();
    }

    private void linkViews() {
        setContentView(R.layout.activity_home);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_home);
        tvNumberApplicants = (TextView) findViewById(R.id.tv_stat_appl);
        tvNumberInterviewConducted = (TextView) findViewById(R.id.tv_stat_interview_conducted);
        tvNumSelected = (TextView) findViewById(R.id.tv_stat_selected);
        tvNumTPShortlisted = (TextView) findViewById(R.id.tv_stat_tp_sl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.nav_home);
        tvNavHeaderName = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_name);
        tvNavHeaderEmailID = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_email_id);
        tvNavHeaderRegNumber = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_reg_number);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
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
                ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(LoginActivity.mCredential, HomeActivity.this);
                readSpreadSheet.delegate = HomeActivity.this;
                readSpreadSheet.execute(params);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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
            case R.id.nav_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
            case R.id.nav_support:
                startActivity(new Intent(HomeActivity.this, SupportActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refresh() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onProcessFinish(ArrayList<ArrayList<ArrayList<String>>> outputList) {
        try {
            int foundIndex = -1;
            ArrayList<ArrayList<String>> output = outputList.get(0);
            size = output.size();
            tvNumberApplicants.setText("" + size);
            Log.d("Number of applicants ", size + "   ");
            boolean stateFlagFound = false;
            for (int i = 0; i < output.size(); i++) {
                ArrayList<String> row = output.get(i);
                if (row.size() > 0 && row.get(0).equals(emailID)) {
                    foundIndex = i;
                    Snackbar.make(coordinatorLayout, "Welcome " + outputList.get(4).get(i).get(0), Snackbar.LENGTH_LONG).show();
                    stateFlagFound = true;
                    break;
                }
            }
            if (!stateFlagFound) {
                Snackbar.make(coordinatorLayout, "Please use the same email address you used to fill the form.  ", Snackbar.LENGTH_INDEFINITE).show();
                //finish();
            } else {
                try {
                    interviewStatus1 = outputList.get(1).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    interviewStatus2 = outputList.get(2).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    tpStatus = outputList.get(3).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    userName = outputList.get(4).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    regNumber = outputList.get(5).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    mobileNumber = outputList.get(6).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    prefDiv1 = outputList.get(7).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                try {
                    prefDiv2 = outputList.get(8).get(foundIndex).get(0);
                } catch (Exception e) {
                }
                if (interviewStatus1.equals("SCHEDULED")) {
                    pref1Schedule = outputList.get(9).get(foundIndex).get(0);
                } else {
                    pref1Schedule = "NOT SCHEDULED";
                }
                if (interviewStatus2.equals("SCHEDULED")) {
                    pref2Schedule = outputList.get(10).get(foundIndex).get(0);
                } else {
                    pref2Schedule = "NOT SCHEDULED";
                }
                tvNavHeaderName.setText(userName);
                try {
                    tvNavHeaderEmailID.setText(outputList.get(0).get(foundIndex).get(0));
                } catch (Exception e) {
                }
                try {
                    tvNavHeaderRegNumber.setText(regNumber);
                } catch (Exception e) {
                }
                cacheData();
            }
            ArrayList<ArrayList<String>> interviewStatus = outputList.get(1);
            int interviewAcceptedCounter = 0, rejectedCounter = 0, maybeCounter = 0;
            for (ArrayList<String> arrayList : interviewStatus) {
                if (arrayList.size() > 0) {
                    if (arrayList.get(0).equals("ACCEPTED")) {
                        interviewAcceptedCounter++;
                    } else if (arrayList.get(0).equals("REJECTED")) {
                        rejectedCounter++;
                    } else if (arrayList.get(0).equals("MAYBE")) {
                        maybeCounter++;
                    }
                }
            }

            tvNumberInterviewConducted.setText((interviewAcceptedCounter + rejectedCounter + maybeCounter) + "");
            tvNumTPShortlisted.setText("" + interviewAcceptedCounter);

            int selectedCounter = 0;
            ArrayList<ArrayList<String>> tpStatus = outputList.get(3);
            for (ArrayList<String> arrayList : tpStatus) {
                if (arrayList.size() > 0) {
                    if (arrayList.get(0).equals("ACCEPTED")) {
                        selectedCounter++;
                    }
                }
            }
            tvNumSelected.setText("" + selectedCounter);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        }
    }
}
