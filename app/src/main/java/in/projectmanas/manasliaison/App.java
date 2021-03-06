package in.projectmanas.manasliaison;

import android.app.Application;
import android.os.Bundle;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.api.services.sheets.v4.SheetsScopes;

import in.projectmanas.manasliaison.backendless_classes.Sheet;
import in.projectmanas.manasliaison.constants.BackendlessCredentials;

/**
 * Created by reuben on 17/9/17.
 */

public class App extends Application {
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};

    //  TODO Use this here instead of in FirstRun
//    public static GoogleAccountCredential mCredential;
    private Sheet cachedResponse = null;
    private Bundle googleSheetsCache;

    @Override
    public void onCreate() {
        Backendless.initApp(this, BackendlessCredentials.appId, BackendlessCredentials.secretKey);
        super.onCreate();
        //TODO Use this here instead of in FirstRun
//        mCredential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff());
    }

    public void getSheetMetadata(final AsyncCallback<Sheet> asyncCallback) {
        if (cachedResponse != null) {
            asyncCallback.handleResponse(cachedResponse);
        } else
            Sheet.findFirstAsync(new AsyncCallback<Sheet>() {
                @Override
                public void handleResponse(Sheet response) {
                    cachedResponse = response;
                    asyncCallback.handleResponse(cachedResponse);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    asyncCallback.handleFault(fault);
                }
            });
    }


    public void setGoogleSheetsCache(Bundle googleSheetsCache) {
        this.googleSheetsCache = googleSheetsCache;
    }


}
