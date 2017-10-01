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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.RecruitmentDetails;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.backendless_classes.UserTable;
import in.projectmanas.manasliaison.constants.ConstantsManas;
import in.projectmanas.manasliaison.listeners.DetailsUpdatedListener;
import in.projectmanas.manasliaison.tasks.UpdateAllDetails;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DetailsUpdatedListener {

    private int phase, size;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNumberApplicants, tvNumberInterviewConducted, tvNumTPShortlisted, tvNumSelected, tvNavHeaderName, tvNavHeaderEmailID, tvNavHeaderRegNumber;
    private String regNumber, userName, emailID, numInterviewConducted, numTPShortlisted, numApplicants, numSelected;
    private String deviceToken;
    private String reScheduleCall;
    private String onlineChallengeDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        emailID = LoginActivity.mCredential.getSelectedAccountName();
        //Snackbar.make(coordinatorLayout, "Loading data please wait", Snackbar.LENGTH_INDEFINITE).show();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        if (getSharedPreferences("UserDetails", Context.MODE_PRIVATE).getString("regNumber", null) == null) {
            linearLayout.setVisibility(View.INVISIBLE);
        } else onNewDetailsFetched();
        getBackendlessDeviceToken();
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        getData();
    }

    private void fillUserTable() {
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
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
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
                if (deviceToken != null && regNumber != null) fillUserTable();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void setCardStatus(int phase) {
        Log.d("Phase ", phase + "");
        //TODO : Make related changes in the view for the corresponding phase.
        //Snackbar.make(coordinatorLayout, "Phase " + phase, Snackbar.LENGTH_LONG).show();
    }

    private void linkViews() {
        setContentView(R.layout.activity_home);
        linearLayout = (LinearLayout) findViewById(R.id.ll_home);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_home);
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
            /*
            case R.id.nav_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
                */
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
    public void onDetailsUpdated() {
        onNewDetailsFetched();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onNewDetailsFetched() {
        getAllCacheData();
        if (deviceToken != null && regNumber != null) fillUserTable();
        setDataToViews();
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void setDataToViews() {
        tvNumberApplicants.setText(numApplicants);
        tvNumberInterviewConducted.setText(numInterviewConducted);
        tvNumTPShortlisted.setText(numTPShortlisted);
        tvNumSelected.setText(numSelected);
        tvNavHeaderEmailID.setText(emailID);
        tvNavHeaderName.setText(userName);
        tvNavHeaderRegNumber.setText(regNumber);
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
        numApplicants = sharedPreferences.getString("numApplicants", "numApplicants");
        numInterviewConducted = sharedPreferences.getString("numInterviewConducted", "numInterviewConducted");
        numTPShortlisted = sharedPreferences.getString("numTPShortlisted", "numTPShortlisted");
        numSelected = sharedPreferences.getString("numSelected", "numSelected");
        getAndCacheRecruitmentDetails();
    }


}
