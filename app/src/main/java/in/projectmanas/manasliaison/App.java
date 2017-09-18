package in.projectmanas.manasliaison;

import android.app.Application;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.api.services.sheets.v4.SheetsScopes;

import in.projectmanas.manasliaison.backendless_classes.Sheet;

/**
 * Created by reuben on 17/9/17.
 */

public class App extends Application {
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};

    //  TODO Use this here instead of in FirstRun
//    public static GoogleAccountCredential mCredential;
    private Sheet cachedResponse = null;

    @Override
    public void onCreate() {
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
}
