package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import in.projectmanas.manasliaison.R;

public class InterviewActivity extends AppCompatActivity {

    private String interviewStatus1, interviewStatus2, tpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        interviewStatus1 = sharedPreferences.getString("interviewStatus1", "interviewStatus1");
        interviewStatus2 = sharedPreferences.getString("interviewStatus2", "interviewStatus2");
        tpStatus = sharedPreferences.getString("tpStatus", "tpStatus");
        Log.d("Received Details ", interviewStatus1 + "\t" + interviewStatus2 + "\t" + tpStatus);
    }

}
