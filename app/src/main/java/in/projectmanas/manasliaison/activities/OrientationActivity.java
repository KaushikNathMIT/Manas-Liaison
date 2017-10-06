package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;

public class OrientationActivity extends AppCompatActivity {

    private boolean cancel;
    private ProgressBar progressBar;

    @Override
    protected void onPause() {
        super.onPause();
        cancel = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        final WebView myWebView = (WebView) findViewById(R.id.wv_orientation);
        progressBar = (ProgressBar) findViewById(R.id.pb_orientation);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Links.findFirstAsync(new AsyncCallback<Links>() {
            @Override
            public void handleResponse(Links response) {
                Log.d("here", response.getPosterURI());
                if (!cancel)
                    myWebView.loadUrl(response.getPosterURI());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
