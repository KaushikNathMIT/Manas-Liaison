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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.RecruitmentDetails;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.constants.BackendlessCredentials;
import in.projectmanas.manasliaison.constants.ConstantsManas;
import in.projectmanas.manasliaison.listeners.SheetDataFetchedListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SheetDataFetchedListener {

    public static GoogleAccountCredential mCredential;
    private int phase, size;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNumberApplicants, tvNumberInterviewConducted, tvNumTPShortlisted, tvNumSelected, tvNavHeaderName, tvNavHeaderEmailID, tvNavHeaderRegNumber;
    private String regNumber, userName, emailID, interviewStatus1, interviewStatus2, tpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        emailID = getIntent().getStringExtra("emailID");
        initializeBackendless();
        getRecruitmentPhase();

        mCredential = FirstRunActivity.mCredential;
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        getCount();
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
                .apply();
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

    private void initializeBackendless() {
        Backendless.initApp(this, BackendlessCredentials.appId, BackendlessCredentials.secretKey);
        Backendless.Messaging.registerDevice(ConstantsManas.gcmId);
    }

    private void setCardStatus(int phase) {
        Log.d("Phase ", phase + "");
        //TODO : Make related changes in the view for the corresponding phase.
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
        tvNavHeaderName = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_name);
        tvNavHeaderEmailID = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_email_id);
        tvNavHeaderRegNumber = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_reg_number);
    }

    private void getCount() {
        ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
            @Override
            public void handleResponse(Sheet response) {
                final String[] params = new String[]{response.getEmailID(), response.getInterviewStatus1(), response.getInterviewStatus2(), response.getTpStatus(), response.getName(), response.getRegNumber()};
                ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, HomeActivity.this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                    //TODO: Have a snackbar saying online challenge is over
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refresh() {

    }

    @Override
    public void onProcessFinish(ArrayList<ArrayList<ArrayList<String>>> outputList) {
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
        if (!stateFlagFound)
            Snackbar.make(coordinatorLayout, "No entry found for the following email address: " + getIntent().getStringExtra("emailID"), Snackbar.LENGTH_LONG).show();
        else {
            interviewStatus1 = outputList.get(1).get(foundIndex).get(0);
            interviewStatus2 = outputList.get(2).get(foundIndex).get(0);
            tpStatus = outputList.get(3).get(foundIndex).get(0);
            userName = outputList.get(4).get(foundIndex).get(0);
            tvNavHeaderName.setText(userName);
            regNumber = outputList.get(5).get(foundIndex).get(0);
            tvNavHeaderEmailID.setText(outputList.get(0).get(foundIndex).get(0));
            tvNavHeaderRegNumber.setText(regNumber);
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
    }
}
