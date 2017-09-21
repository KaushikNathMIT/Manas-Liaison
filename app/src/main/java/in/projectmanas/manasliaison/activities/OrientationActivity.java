package in.projectmanas.manasliaison.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;

public class OrientationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        final WebView myWebView = (WebView) findViewById(R.id.wv_orientation);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        Links.findFirstAsync(new AsyncCallback<Links>() {
            @Override
            public void handleResponse(Links response) {
                Log.d("here", response.getPosterURI());
                myWebView.loadUrl(response.getPosterURI());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
