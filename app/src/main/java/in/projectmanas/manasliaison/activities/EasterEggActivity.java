package in.projectmanas.manasliaison.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import in.projectmanas.manasliaison.R;

public class EasterEggActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        try {
            actionBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final TextView hint = (TextView) findViewById(R.id.easter_hint);
        final TextView tooLate = (TextView) findViewById(R.id.too_late);


        final View background = findViewById(R.id.easter_background);
        background.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                hint.setVisibility(View.INVISIBLE);
                tooLate.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tooLate.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }, 100);
                return true;
            }
        });
    }
}
