package in.projectmanas.manasliaison.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.projectmanas.manasliaison.App;
import in.projectmanas.manasliaison.MyCredential;
import in.projectmanas.manasliaison.R;
import in.projectmanas.manasliaison.backendless_classes.Links;
import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.constants.BackendlessCredentials;
import in.projectmanas.manasliaison.constants.ConstantsManas;
import in.projectmanas.manasliaison.listeners.SheetDataFetchedListener;
import in.projectmanas.manasliaison.tasks.ReadSpreadSheet;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, SheetDataFetchedListener {
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};
    public static GoogleAccountCredential mCredential;
    public static MyCredential myCredential;
    ProgressDialog mProgress;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private Button login;

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Backendless.initApp(this, BackendlessCredentials.appId, BackendlessCredentials.secretKey);
        linkViews();
        myCredential = new MyCredential();
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        progressBar.setVisibility(View.INVISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResultsFromApi();
            }
        });
        try {
            if (getPreferences(Context.MODE_PRIVATE).getString(ConstantsManas.PREF_ACCOUNT_NAME, null) != null) {
                mCredential.setSelectedAccountName(getPreferences(Context.MODE_PRIVATE).getString(ConstantsManas.PREF_ACCOUNT_NAME, null));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
                mCredential.setSelectedAccountName(null);
            }
        } catch (Exception e) {
            getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
            mCredential.setSelectedAccountName(null);
        }
    }

    private void linkViews() {
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.pb_circular_login);
        login = (Button) findViewById(R.id.button_login);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_login);
    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_LONG).show();
        } else {

            checkEmailID();
        }
    }

    private void checkEmailID() {
        //Snackbar.make(coordinatorLayout, "Authenticating.. Please wait.", Snackbar.LENGTH_INDEFINITE).show();
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        ((App) getApplication()).getSheetMetadata(new AsyncCallback<Sheet>() {
            @Override
            public void handleResponse(Sheet response) {
                final String[] params = new String[]{response.getEmailID()};
                ReadSpreadSheet readSpreadSheet = new ReadSpreadSheet(mCredential, LoginActivity.this);
                readSpreadSheet.delegate = LoginActivity.this;
                readSpreadSheet.execute(params);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(ConstantsManas.PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    /*mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");*/
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }


    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                LoginActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void onProcessFinish(ArrayList<ArrayList<ArrayList<String>>> outputList) {
        boolean stateFlagFound = false;
        ArrayList<ArrayList<String>> output = outputList.get(0);
        for (int i = 0; i < output.size(); i++) {
            ArrayList<String> row = output.get(i);
            if (row.size() > 0 && row.get(0).equals(mCredential.getSelectedAccountName())) {
                //Snackbar.make(coordinatorLayout, "Welcome " + outputList.get(4).get(i).get(0), Snackbar.LENGTH_LONG).show();
                stateFlagFound = true;
                break;
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
        if (!stateFlagFound) {
            getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
            mCredential.setSelectedAccount(null);
            Snackbar.make(coordinatorLayout, "Please use the same email address you used to fill the form.  ", Snackbar.LENGTH_INDEFINITE).setAction("Fill Form", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Links.findFirstAsync(new AsyncCallback<Links>() {
                        @Override
                        public void handleResponse(Links response) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.getRegistrationForm())));
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Snackbar.make(coordinatorLayout, fault.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        }
                    });
                }
            }).show();
        } else {
            SharedPreferences settings =
                    getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ConstantsManas.PREF_ACCOUNT_NAME, mCredential.getSelectedAccountName());
            editor.apply();
            Backendless.Messaging.registerDevice(ConstantsManas.gcmId);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("emailID", mCredential.getSelectedAccountName());
            //Log.d("emailID", mCredential.getSelectedAccountName());
            startActivity(intent);
            finish();
        }
    }
}