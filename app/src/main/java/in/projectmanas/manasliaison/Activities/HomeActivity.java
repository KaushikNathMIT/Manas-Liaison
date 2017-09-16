package in.projectmanas.manasliaison.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.backendless.Backendless;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import in.projectmanas.manasliaison.BackendlessClasses.RecruitmentDetails;
import in.projectmanas.manasliaison.Constants.BackendlessCredentials;
import in.projectmanas.manasliaison.Constants.ConstantsManas;
import in.projectmanas.manasliaison.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    public static GoogleAccountCredential mCredential;
    private int phase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();

        Backendless.initApp(this, BackendlessCredentials.appId, BackendlessCredentials.secretKey);
        Backendless.Messaging.registerDevice(ConstantsManas.gcmId);
        phase = RecruitmentDetails.findFirst().getPhase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mCredential = FirstRunActivity.mCredential;
        //Log.d("crdential here ", getIntent().getStringExtra(ConstantsManas.ACCNAME));
        fetchSampleDetails();
    }

    private void linkViews() {
        setContentView(R.layout.activity_home);
    }

    private void fetchSampleDetails() {
        String[] params = new String[]{"Interview!P3"};
        ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, this);
        readSpreadSheet.delegate = this;
        readSpreadSheet.execute(params);
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
    public void processFinish(ArrayList<ArrayList<String>> output) {
        Log.d("aaa", output.size() + "");
    }
}
