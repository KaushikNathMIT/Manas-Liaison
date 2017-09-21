package in.projectmanas.manasliaison.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import java.util.ArrayList;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.listeners.SheetDataFetchedListener;
import in.projectmanas.manasliaison.tasks.ReadSpreadSheet;

import static in.projectmanas.manasliaison.activities.HomeActivity.mCredential;

public class TaskPhaseActivity extends AppCompatActivity implements SheetDataFetchedListener {

    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> aiList, snaList, mechList;
    private ExpandableHeightListView aiListView, snaListView, mechListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkViews();
        initializeArrayLists();
        getDetails();
    }

    private void initializeArrayLists() {
        aiList = new ArrayList<>();
        snaList = new ArrayList<>();
        mechList = new ArrayList<>();
    }

    private void linkViews() {
        setContentView(R.layout.activity_task_phase);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_task_phase);
        aiListView = (ExpandableHeightListView) findViewById(R.id.lv_ai_tp);
        snaListView = (ExpandableHeightListView) findViewById(R.id.lv_sna_tp);
        mechListView = (ExpandableHeightListView) findViewById(R.id.lv_mech_tp);
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
        for (int i = 0; i < outputList.get(0).size(); i++) {
            try {
                if (outputList.get(1).get(i).get(0).trim().equals("ACCEPTED")) {
                    if (outputList.get(2).get(i).get(0).trim().equals("Artificial Intelligence")) {
                        aiList.add(outputList.get(0).get(i).get(0));
                    } else if (outputList.get(2).get(i).get(0).trim().equals("Sensing & Automation")) {
                        snaList.add(outputList.get(0).get(i).get(0));
                    } else if (outputList.get(2).get(i).get(0).trim().equals("Mechanical")) {
                        mechList.add(outputList.get(0).get(i).get(0));
                    }
                }
            } catch (Exception e) {
                Log.e("Exception : ", e.getMessage());
            }
        }
        setListViews();
    }

    private void setListViews() {
        aiListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, aiList.toArray()));
        snaListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, snaList.toArray()));
        mechListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mechList.toArray()));
        aiListView.setExpanded(true);
        snaListView.setExpanded(true);
        mechListView.setExpanded(true);
    }
}
