package in.projectmanas.manasliaison.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import in.projectmanas.manasliaison.R;

public class CCPLActivity extends AppCompatActivity {
    private TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccpl);
        textInputLayout = (TextInputLayout) findViewById(R.id.til_competitive_coding_profile_link);

    }
}
