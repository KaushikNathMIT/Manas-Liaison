package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;

public class OnlineChallengeOngoing extends AppCompatActivity {
    private Button onlineChallengeRedirectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_challenge_ongoing);
        linkViews();
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
        onlineChallengeRedirectButton = (Button) findViewById(R.id.button_reg_hacker_rank);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
