package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import in.projectmanas.manasliaison.R;

public class ProfileActivity extends AppCompatActivity {
    private String userName, emailID, regNumber, mobileNumber;
    private TextView tvUserName, tvEmailID, tvRegNumber, tvMobileNumber;

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
    }

    private void linkViews() {
        setContentView(R.layout.activity_profile);
        tvUserName = (TextView) findViewById(R.id.tv_name);
        tvEmailID = (TextView) findViewById(R.id.tv_email_id);
        tvRegNumber = (TextView) findViewById(R.id.tv_reg_number);
        tvMobileNumber = (TextView) findViewById(R.id.tv_mobile_number);
    }
}
