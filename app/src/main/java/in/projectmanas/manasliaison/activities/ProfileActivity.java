package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;

public class ProfileActivity extends AppCompatActivity {
    private String userName, emailID, regNumber, mobileNumber;
    private TextView tvUserName, tvEmailID, tvRegNumber, tvMobileNumber;
    private Button editDetailsButton;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("name", "name");
        emailID = sharedPreferences.getString("emailID", "emailID");
        regNumber = sharedPreferences.getString("regNumber", "regNumber");
        mobileNumber = sharedPreferences.getString("mobileNumber", "mobileNumber");
        Log.d("Details received", userName + "\t" + emailID + "\t" + regNumber);
        tvUserName.setText(userName);
        tvEmailID.setText(emailID);
        tvRegNumber.setText(regNumber);
        tvMobileNumber.setText(mobileNumber);
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Links.findFirstAsync(new AsyncCallback<Links>() {
                    @Override
                    public void handleResponse(Links response) {
                        openWebView(response.getRegistrationForm());
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    }
                });
            }
        });
    }

    private void openWebView(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void linkViews() {
        setContentView(R.layout.activity_profile);
        tvUserName = (TextView) findViewById(R.id.tv_name);
        tvEmailID = (TextView) findViewById(R.id.tv_email_id);
        tvRegNumber = (TextView) findViewById(R.id.tv_reg_number);
        tvMobileNumber = (TextView) findViewById(R.id.tv_mobile_number);
        editDetailsButton = (Button) findViewById(R.id.button_edit_details);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_profile);
    }
}
