package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import in.projectmanas.manasliaison.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvMission, tvVision;
    private ImageButton fb, gplus, youtube, twitter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        setListeners();
    }

    private void setListeners() {
        fb.setOnClickListener(this);
        gplus.setOnClickListener(this);
        youtube.setOnClickListener(this);
        twitter.setOnClickListener(this);
    }

    private void linkViews() {
        setContentView(R.layout.activity_about);
        tvVision = (TextView) findViewById(R.id.tv_vision);
        tvMission = (TextView) findViewById(R.id.tv_mission);
        fb = (ImageButton) findViewById(R.id.ib_fb);
        gplus = (ImageButton) findViewById(R.id.ib_gplus);
        youtube = (ImageButton) findViewById(R.id.ib_yt);
        twitter = (ImageButton) findViewById(R.id.ib_twitter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ib_fb:
                openWebView("https://www.facebook.com/projectmanas/");
                break;
            case R.id.ib_gplus:
                openWebView("https://plus.google.com/+ProjectMANASManipal");
                break;
            case R.id.ib_yt:
                openWebView("https://www.youtube.com/channel/UCpgA3-I00lUVgXVeg1CNaFw");
                break;
            case R.id.ib_twitter:
                openWebView("https://twitter.com/projectmanas");
                break;
        }
    }

    private void openWebView(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
