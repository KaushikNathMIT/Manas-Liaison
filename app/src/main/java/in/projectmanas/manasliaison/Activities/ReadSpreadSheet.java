package in.projectmanas.manasliaison.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.projectmanas.manasliaison.BackendlessClasses.Sheet;

import static in.projectmanas.manasliaison.Activities.FirstRunActivity.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * Created by knnat on 9/14/2017.
 */

public class ReadSpreadSheet extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {
    public AsyncResponse delegate = null;
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private String range;
    private Activity context;

    ReadSpreadSheet(GoogleAccountCredential credential, Activity context) {
        this.context = context;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Project Manas Hub")
                .build();
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String[] params) {
        try {
            range = params[0];
            return getDataFromApi();
        } catch (Exception e) {
            Log.e("Error", e.toString());
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    private ArrayList<ArrayList<String>> getDataFromApi() throws IOException {
        String spreadsheetId = Sheet.findFirst().getSpreadsheetId();
        //Log.d("Id sheet:", spreadsheetId);
        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        ArrayList<ArrayList<String>> valueStrings = new ArrayList<>();
        if (values != null) {
            //Log.d("size", values.size() + " ");
            for (List row : values) {
                //Log.d("adasd", row.get(0) + " " + row.get(1) + " " + row.get(2));
                ArrayList<String> currentRow = new ArrayList<>();
                for (Object ob : row) {
                    //Log.d("Output here", ob.toString());
                    currentRow.add(ob.toString());
                }
                valueStrings.add(currentRow);
            }
        }
        //Log.d("Output recieved of size", valueStrings.size() + "");
        return valueStrings;
    }


    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> output) {
        //Log.d("Output recieved of size", output.size() + "");
        delegate.processFinish(output);
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                context,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    protected void onCancelled() {

        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                context.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        FirstRunActivity.REQUEST_AUTHORIZATION);
            } else {
                Log.e("Error", "The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            Log.e("Error", "Request cancelled.");
        }
    }

}
