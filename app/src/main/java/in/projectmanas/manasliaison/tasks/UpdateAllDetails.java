package in.projectmanas.manasliaison.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.projectmanas.manasliaison.MyCredential;
import in.projectmanas.manasliaison.activities.LoginActivity;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.listeners.DetailsUpdatedListener;

import static in.projectmanas.manasliaison.activities.LoginActivity.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * Created by knnat on 9/30/2017.
 */

public class UpdateAllDetails extends AsyncTask<String, Void, ArrayList<ArrayList<ArrayList<String>>>> {
    public DetailsUpdatedListener delegate = null;
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private List<String> ranges;
    private Activity context;
    private String regNumber, userName, emailID, interviewStatus1, interviewStatus2, tpStatus, mobileNumber, prefDiv1, prefDiv2, pref1Schedule, pref2Schedule, numInterviewConducted, numTPShortlisted, numApplicants, numSelected;

    public UpdateAllDetails(Activity context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        emailID = sharedPreferences.getString("emailID", "emailID");
        if (emailID.equals("emailID")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            context.finish();
        }
        this.context = context;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, new MyCredential())
                .setApplicationName("Manas-Liaison")
                .build();
    }


    @Override
    protected ArrayList<ArrayList<ArrayList<String>>> doInBackground(String[] params) {
        try {
            ranges = Arrays.asList(params);
            return getDataFromApi();
        } catch (Exception e) {
            Log.e("Error", e.toString());
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    private ArrayList<ArrayList<ArrayList<String>>> getDataFromApi() throws IOException {
        String spreadsheetId = Sheet.findFirst().getSpreadsheetId();
        //Log.d("Id sheet:", spreadsheetId);
        BatchGetValuesResponse response = this.mService.spreadsheets().values()
                .batchGet(spreadsheetId)
                .setRanges(ranges)
                .execute();

        ArrayList<List<List<Object>>> values = new ArrayList<>();
        for (int i = 0; i < response.getValueRanges().size(); i++) {
            values.add(response.getValueRanges().get(i).getValues());
        }
        ArrayList<ArrayList<ArrayList<String>>> valueStrings = new ArrayList<>();
        if (values.size() > 0) {
            //Log.d("size", values.size() + " ");
            for (int i = 0; i < values.size(); i++) {
                ArrayList<ArrayList<String>> currentTable = new ArrayList<>();
                for (List row : values.get(i)) {
                    //Log.d("adasd", row.size()+ "");
                    ArrayList<String> currentRow = new ArrayList<>();
                    for (Object ob : row) {
                        //Log.d("Output here", ob.toString());
                        currentRow.add(ob.toString());
                    }
                    currentTable.add(currentRow);
                }
                valueStrings.add(currentTable);
            }
        }
        //Log.d("Output recieved of size", valueStrings.size() + "");
        return valueStrings;
    }


    @Override
    protected void onPostExecute(ArrayList<ArrayList<ArrayList<String>>> outputList) {
        int interviewAcceptedCounter = 0, rejectedCounter = 0, maybeCounter = 0, selectedCounter = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        emailID = sharedPreferences.getString("emailID", "emailID");
        if (emailID.equals("emailID")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            context.finish();
        }
        try {
            int foundIndex = -1;
            ArrayList<ArrayList<String>> output = outputList.get(0);
            numApplicants = output.size() + "";
            for (int i = 0; i < output.size(); i++) {
                ArrayList<String> row = output.get(i);
                if (row.size() > 0 && row.get(0).equals(emailID)) {
                    foundIndex = i;
                    break;
                }
            }

            try {
                interviewStatus1 = outputList.get(1).get(foundIndex).get(0);
            } catch (Exception e) {
                interviewStatus1 = "";
            }
            try {
                interviewStatus2 = outputList.get(2).get(foundIndex).get(0);
            } catch (Exception e) {
                interviewStatus2 = "";
            }
            try {
                tpStatus = outputList.get(3).get(foundIndex).get(0);
            } catch (Exception e) {
                tpStatus = "";
            }
            try {
                userName = outputList.get(4).get(foundIndex).get(0);
            } catch (Exception e) {
                userName = "";
            }
            try {
                regNumber = outputList.get(5).get(foundIndex).get(0);
            } catch (Exception e) {
                regNumber = "";
            }
            try {
                mobileNumber = outputList.get(6).get(foundIndex).get(0);
            } catch (Exception e) {
                mobileNumber = "";
            }
            try {
                prefDiv1 = outputList.get(7).get(foundIndex).get(0);
            } catch (Exception e) {
                prefDiv1 = "";
            }
            try {
                prefDiv2 = outputList.get(8).get(foundIndex).get(0);
            } catch (Exception e) {
                prefDiv2 = "";
            }
            if (interviewStatus1.equals("SCHEDULED")) {
                pref1Schedule = outputList.get(9).get(foundIndex).get(0);
            } else {
                pref1Schedule = "NOT SCHEDULED";
            }
            if (interviewStatus2.equals("SCHEDULED")) {
                pref2Schedule = outputList.get(10).get(foundIndex).get(0);
            } else {
                pref2Schedule = "NOT SCHEDULED";
            }

            ArrayList<ArrayList<String>> interviewStatus = outputList.get(1);

            for (ArrayList<String> arrayList : interviewStatus) {
                if (arrayList.size() > 0) {
                    if (arrayList.get(0).equals("ACCEPTED")) {
                        interviewAcceptedCounter++;
                    } else if (arrayList.get(0).equals("REJECTED")) {
                        rejectedCounter++;
                    } else if (arrayList.get(0).equals("MAYBE")) {
                        maybeCounter++;
                    }
                }
            }

            ArrayList<ArrayList<String>> tpStatus = outputList.get(3);
            for (ArrayList<String> arrayList : tpStatus) {
                if (arrayList.size() > 0) {
                    if (arrayList.get(0).equals("ACCEPTED")) {
                        selectedCounter++;
                    }
                }
            }
            numInterviewConducted = (interviewAcceptedCounter + rejectedCounter + maybeCounter) + "";
            numTPShortlisted = "" + interviewAcceptedCounter;
            numSelected = "" + selectedCounter;
        } catch (Exception e) {

        }
        cacheAllData();
        delegate.onDetailsUpdated();
    }

    private void cacheAllData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor
                .putString("name", userName)
                .putString("emailID", emailID)
                .putString("interviewStatus1", interviewStatus1)
                .putString("interviewStatus2", interviewStatus2)
                .putString("tpStatus", tpStatus)
                .putString("regNumber", regNumber)
                .putString("mobileNumber", mobileNumber)
                .putString("prefDiv1", prefDiv1)
                .putString("prefDiv2", prefDiv2)
                .putString("pref1Schedule", pref1Schedule)
                .putString("pref2Schedule", pref2Schedule)
                .putString("numApplicants", numApplicants)
                .putString("numInterviewConducted", numInterviewConducted)
                .putString("numTPShortlisted", numTPShortlisted)
                .putString("numSelected", numSelected)
                .apply();
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
                        LoginActivity.REQUEST_AUTHORIZATION);
            } else {
                Log.e("Error", "The following error occurred:\n"
                        + mLastError.getMessage());
                Toast.makeText(context, mLastError.toString(), Toast.LENGTH_LONG).show();
                context.getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
                context.startActivity(new Intent(context, LoginActivity.class));

            }
        } else {
            Log.e("Error", "Request cancelled.");
        }
    }
}
