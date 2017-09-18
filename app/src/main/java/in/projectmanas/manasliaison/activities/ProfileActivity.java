package in.projectmanas.manasliaison.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import in.projectmanas.manasliaison.R;

public class ProfileActivity extends AppCompatActivity {
    private String userName, emailID, regNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("name", "name");
        emailID = sharedPreferences.getString("emailID", "emailID");
        regNumber = sharedPreferences.getString("regNumber", "regNumber");
        Log.d("Details received", userName + "\t" + emailID + "\t" + regNumber);
        //TODO : Do whatever you want to do with this data.
    }
}
