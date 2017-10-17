package in.projectmanas.manasliaison.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

import in.projectmanas.manasliaison.backendless_classes.UserTable;

/**
 * Created by knnat on 10/5/2017.
 */

public class UpdateSchedule extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String notifySection;

    public UpdateSchedule(Context context, String notifySection) {
        this.context = context;
        this.notifySection = notifySection;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String whereClause = "registrationNumber = " + sharedPreferences.getString("regNumber", "regNumber");
        queryBuilder.setWhereClause(whereClause);
        UserTable.findAsync(queryBuilder, new AsyncCallback<List<UserTable>>() {
            @Override
            public void handleResponse(List<UserTable> response) {
                if (response.size() > 0) {
                    switch (notifySection) {
                        case "pref1Confirm":
                            response.get(0).setPref1Confirm("NOTIFIED");
                            break;
                        case "pref2Confirm":
                            response.get(0).setPref2Confirm("NOTIFIED");
                            break;
                        case "div1":
                            response.get(0).setDiv1("NOTIFIED");
                            break;
                        case "div2":
                            response.get(0).setDiv2("NOTIFIED");
                            break;
                    }
                    response.get(0).saveAsync(new AsyncCallback<UserTable>() {
                        @Override
                        public void handleResponse(UserTable response) {
                            Log.d("status", "Schedule updated to notified");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("status", fault.getMessage());
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        return null;
    }
}
