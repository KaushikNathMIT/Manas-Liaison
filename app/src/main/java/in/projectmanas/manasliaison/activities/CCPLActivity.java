package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import in.projectmanas.manasliaison.R;

public class CCPLActivity extends AppCompatActivity {
    private TextInputLayout textInputLayout;
    private Button submit;
    private CoordinatorLayout coordinatorLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccpl);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_ccpl);
        submit = (Button) findViewById(R.id.button_ccpl);
        textInputLayout = (TextInputLayout) findViewById(R.id.til_competitive_coding_profile_link);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setResult(1, new Intent().putExtra("ccpl", textInputLayout.getEditText().getText().toString()));
                    finish();
                } catch (Exception e) {
                    Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        });
    }
}
