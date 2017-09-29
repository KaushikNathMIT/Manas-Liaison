package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.fragments.InterviewPendingFragment;
import in.projectmanas.manasliaison.fragments.InterviewRejectedFragment;
import in.projectmanas.manasliaison.fragments.InterviewResultPendingFragment;
import in.projectmanas.manasliaison.fragments.InterviewScheduledFragment;
import in.projectmanas.manasliaison.fragments.InterviewSelectedFragment;

public class InterviewActivity extends AppCompatActivity {

    private String interviewStatus1, interviewStatus2, tpStatus, prefDiv1, prefDiv2;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView tvInterviewStatus1, tvInterviewStatus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        interviewStatus1 = sharedPreferences.getString("interviewStatus1", "interviewStatus1");
        interviewStatus2 = sharedPreferences.getString("interviewStatus2", "interviewStatus2");
        tpStatus = sharedPreferences.getString("tpStatus", "tpStatus");
        prefDiv1 = sharedPreferences.getString("prefDiv1", "prefDiv1");
        prefDiv2 = sharedPreferences.getString("prefDiv2", "prefDiv2");
        linkViews();
        Log.d("Received Details ", interviewStatus1 + "\t" + interviewStatus2 + "\t" + tpStatus);
        setStatusTextColor();
    }

    private void setStatusTextColor() {
        tvInterviewStatus1.setText(interviewStatus1);
        tvInterviewStatus2.setText(interviewStatus2);
        tvInterviewStatus1.setBackgroundColor(getColor(interviewStatus1));
        tvInterviewStatus2.setBackgroundColor(getColor(interviewStatus2));
    }

    private int getColor(String interviewStatus) {
        switch (interviewStatus) {
            case "ACCEPTED":
                return Color.GREEN;
            case "REJECTED":
                return Color.RED;
            case "PENDING":
                return Color.YELLOW;
            case "SCHEDULED":
                return Color.BLUE;
            case "RESULT PENDING":
                return Color.GRAY;
        }
        return Color.BLACK;
    }

    private void linkViews() {
        setContentView(R.layout.activity_interview);
        toolbar = (Toolbar) findViewById(R.id.interview_toolbar_layout);
        toolbar.setTitle("Interview");
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.interview_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tvInterviewStatus1 = (TextView) findViewById(R.id.tv_interview_stat1);
        tvInterviewStatus2 = (TextView) findViewById(R.id.tv_interview_stat2);
        addFragmentsToTabs();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
    }

    private void addFragmentsToTabs() {
        final ArrayList<Fragment> fragments = new ArrayList<>();
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
        if (!prefDiv1.equals(prefDiv2)) {
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
}
