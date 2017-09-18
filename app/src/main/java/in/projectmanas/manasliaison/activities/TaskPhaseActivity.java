package in.projectmanas.manasliaison.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.listeners.SheetDataFetchedListener;

import static in.projectmanas.manasliaison.activities.HomeActivity.mCredential;

public class TaskPhaseActivity extends AppCompatActivity implements SheetDataFetchedListener {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        getDetails();
    }

    private void linkViews() {
        setContentView(R.layout.activity_task_phase);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_task_phase);
    }

    private void getDetails() {
        ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
            @Override
            public void handleResponse(Sheet response) {
                final String[] params = new String[]{response.getName(), response.getTpStatus(), response.getDiv()};
                ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, TaskPhaseActivity.this);
                readSpreadSheet.delegate = TaskPhaseActivity.this;
                readSpreadSheet.execute(params);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onProcessFinish(ArrayList<ArrayList<ArrayList<String>>> outputList) {
        ArrayList<ArrayList<String>> output = outputList.get(0);
        Log.d("Received here", output.get(0).get(0) + "\t" + outputList.get(1).get(0).get(0) + "\t" + outputList.get(2).get(0).get(0));
    }
}
