package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Date;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;
import in.projectmanas.manasliaison.backendless_classes.RecruitmentDetails;

public class OnlineChallengeOngoing extends AppCompatActivity {
    private Button onlineChallengeRedirectButton;
    private TextView tvOLDaysRemaining;
    private CoordinatorLayout coordinatorLayout;
    private View pbOLDaysRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        RecruitmentDetails.findFirstAsync(new AsyncCallback<RecruitmentDetails>() {
            @Override
            public void handleResponse(RecruitmentDetails response) {
                pbOLDaysRemaining.setVisibility(View.INVISIBLE);
                tvOLDaysRemaining.setText(String.valueOf(
                        (response.getOnlineChallengeDate().getTime() - new Date().getTime()) / ((24 * 60 * 60 * 1000))
                ));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        onlineChallengeRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Links.findFirstAsync(new AsyncCallback<Links>() {
                    @Override
                    public void handleResponse(Links response) {
                        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                        httpIntent.setData(Uri.parse(response.getOnlineChallenge()));
                        startActivity(httpIntent);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void linkViews() {
        setContentView(R.layout.activity_online_challenge_ongoing);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_ongoing_ol);
        onlineChallengeRedirectButton = (Button) findViewById(R.id.button_reg_hacker_rank);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pbOLDaysRemaining = findViewById(R.id.pb_online_challenge);
        tvOLDaysRemaining = (TextView) findViewById(R.id.tv_ol_days_remaining);
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
}
