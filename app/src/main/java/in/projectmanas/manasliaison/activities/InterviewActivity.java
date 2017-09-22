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

import java.util.ArrayList;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.fragments.InterviewFragment1;
import in.projectmanas.manasliaison.fragments.InterviewFragment2;

public class InterviewActivity extends AppCompatActivity {

    private String interviewStatus1, interviewStatus2, tpStatus, prefDiv1, prefDiv2;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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
    }

    private void linkViews() {
        setContentView(R.layout.activity_interview);
        toolbar = (Toolbar) findViewById(R.id.interview_toolbar_layout);
        toolbar.setTitle("Manas Interview");
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.interview_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(InterviewFragment1.getInstance());
        fragments.add(InterviewFragment2.getInstance());
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
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
    }
}
