package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.fragments.InterviewPendingFragment;
import in.projectmanas.manasliaison.fragments.InterviewRejectedFragment;
import in.projectmanas.manasliaison.fragments.InterviewResultPendingFragment;
import in.projectmanas.manasliaison.fragments.InterviewScheduledFragment;
import in.projectmanas.manasliaison.fragments.InterviewSelectedFragment;
import in.projectmanas.manasliaison.listeners.DetailsUpdatedListener;
import in.projectmanas.manasliaison.tasks.UpdateAllDetails;

public class InterviewActivity extends AppCompatActivity implements DetailsUpdatedListener {

    private String interviewStatus1, interviewStatus2, prefDiv1, prefDiv2;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView tvInterviewStatus1, tvInterviewStatus2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if (!sharedPreferences.getString("prefDiv1", "prefDiv1").equals("prefDiv1"))
            getCachedData();
        else {
            swipeRefreshLayout.setRefreshing(true);
            ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
                @Override
                public void handleResponse(Sheet response) {
                    final String[] params = new String[]{response.getEmailID(), response.getInterviewStatus1(), response.getInterviewStatus2(), response.getTpStatus(), response.getName(), response.getRegNumber(), response.getMobileNumber(), response.getPrefDiv1(), response.getPrefDiv2(), response.getPref1Schedule(), response.getPref2Schedule()};
                    UpdateAllDetails updateAllDetails = new UpdateAllDetails(InterviewActivity.this);
                    updateAllDetails.delegate = InterviewActivity.this;
                    updateAllDetails.execute(params);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
                    @Override
                    public void handleResponse(Sheet response) {

                        final String[] params = new String[]{response.getEmailID(), response.getInterviewStatus1(), response.getInterviewStatus2(), response.getTpStatus(), response.getName(), response.getRegNumber(), response.getMobileNumber(), response.getPrefDiv1(), response.getPrefDiv2(), response.getPref1Schedule(), response.getPref2Schedule()};
                        UpdateAllDetails updateAllDetails = new UpdateAllDetails(InterviewActivity.this);
                        updateAllDetails.delegate = InterviewActivity.this;
                        updateAllDetails.execute(params);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });
    }


    private void setStatusTextAndColor() {
        tvInterviewStatus1.setText(interviewStatus1);
        tvInterviewStatus2.setText(interviewStatus2);
        tvInterviewStatus1.setBackgroundColor(Color.parseColor(getColor(interviewStatus1)));
        tvInterviewStatus2.setBackgroundColor(Color.parseColor(getColor(interviewStatus2)));
    }

    private void getCachedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        interviewStatus1 = sharedPreferences.getString("interviewStatus1", "interviewStatus1");
        interviewStatus2 = sharedPreferences.getString("interviewStatus2", "interviewStatus2");
        //tpStatus = sharedPreferences.getString("tpStatus", "tpStatus");
        prefDiv1 = sharedPreferences.getString("prefDiv1", "prefDiv1");
        prefDiv2 = sharedPreferences.getString("prefDiv2", "prefDiv2");
        addFragmentsToTabs();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        //Log.d("Received Details ", interviewStatus1 + "\t" + interviewStatus2 + "\t" + tpStatus);
        setStatusTextAndColor();
    }

    private String getColor(String interviewStatus) {
        switch (interviewStatus) {
            case "ACCEPTED":
                return "#d6ffee";
            case "REJECTED":
                return "#ffe1f3";
            case "PENDING":
                return "#fffbbe";
            case "SCHEDULED":
                return "#d4eeff";
            case "RESULT PENDING":
                return "#e9e8ff";
        }
        return "#000000";
    }

    private void linkViews() {
        setContentView(R.layout.activity_interview);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_activity_interview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_interview);
        toolbar = (Toolbar) findViewById(R.id.interview_toolbar_layout);
        toolbar.setTitle("Interview");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.interview_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tvInterviewStatus1 = (TextView) findViewById(R.id.tv_interview_stat1);
        tvInterviewStatus2 = (TextView) findViewById(R.id.tv_interview_stat2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addFragmentsToTabs() {
        final ArrayList<Fragment> fragments = new ArrayList<>();
        if (!prefDiv1.equals("")) {
            switch (interviewStatus1) {
                case "ACCEPTED":
                    //textViewStatusDesc.setText("Congratulations, you’ve been selected for the task phase of this division. We are awaiting your response to our offer.");
                    InterviewSelectedFragment interviewSelectedFragment = new InterviewSelectedFragment();
                    interviewSelectedFragment.setDetails(this, interviewStatus1, interviewStatus2, 1);
                    fragments.add(interviewSelectedFragment);
                    break;
                case "REJECTED":
                    //textViewStatusDesc.setText("We regret to inform you that you have not been selected for the task phase round.");
                    InterviewRejectedFragment interviewRejectedFragment = InterviewRejectedFragment.getInstance();
                    fragments.add(interviewRejectedFragment);
                    break;
                case "RESULT PENDING":
                    InterviewResultPendingFragment interviewResultPendingFragment = new InterviewResultPendingFragment();
                    fragments.add(interviewResultPendingFragment);
                    //textViewStatusDesc.setText("We’ve completed your interview. The result of your interview will be announced, once all interviews have been completed.");
                    break;
                case "PENDING":
                    InterviewPendingFragment interviewPendingFragment = new InterviewPendingFragment();
                    fragments.add(interviewPendingFragment);
                    //textViewStatusDesc.setText("Our bots are busy at work scheduling your interview, take it easy on them, they aren’t human");
                    break;
                case "SCHEDULED":
                    InterviewScheduledFragment interviewScheduledFragment = new InterviewScheduledFragment();
                    fragments.add(interviewScheduledFragment);
            }
        }
        if (!prefDiv1.equals(prefDiv2) && !prefDiv2.equals("")) {
            tvInterviewStatus2.setVisibility(View.VISIBLE);

            switch (interviewStatus2) {
                case "ACCEPTED":
                    //textViewStatusDesc.setText("Congratulations, you’ve been selected for the task phase of this division. We are awaiting your response to our offer.");
                    InterviewSelectedFragment interviewSelectedFragment = new InterviewSelectedFragment();
                    interviewSelectedFragment.setDetails(this, interviewStatus1, interviewStatus2, 2);
                    fragments.add(interviewSelectedFragment);
                    break;
                case "REJECTED":
                    //textViewStatusDesc.setText("We regret to inform you that you have not been selected for the task phase round.");
                    InterviewRejectedFragment interviewRejectedFragment = InterviewRejectedFragment.getInstance();
                    fragments.add(interviewRejectedFragment);
                    break;
                case "RESULT PENDING":
                    InterviewResultPendingFragment interviewResultPendingFragment = new InterviewResultPendingFragment();
                    fragments.add(interviewResultPendingFragment);
                    //textViewStatusDesc.setText("We’ve completed your interview. The result of your interview will be announced, once all interviews have been completed.");
                    break;
                case "PENDING":
                    InterviewPendingFragment interviewPendingFragment = new InterviewPendingFragment();
                    fragments.add(interviewPendingFragment);
                    //textViewStatusDesc.setText("Our bots are busy at work scheduling your interview, take it easy on them, they aren’t human");
                    break;
                case "SCHEDULED":
                    InterviewScheduledFragment interviewScheduledFragment = new InterviewScheduledFragment();
                    interviewScheduledFragment.setIndex(2);
                    fragments.add(interviewScheduledFragment);
            }
        } else {
            tvInterviewStatus2.setVisibility(View.GONE);
        }
        final String[] titles = {prefDiv1, prefDiv2};
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
    }

    @Override
    public void onDetailsUpdated() {
        swipeRefreshLayout.setRefreshing(false);
        finish();
        startActivity(getIntent());
    }
}
