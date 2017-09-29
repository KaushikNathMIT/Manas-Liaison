package in.projectmanas.manasliaison;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

import java.io.IOException;

/**
 * Created by knnat on 9/29/2017.
 */

public class MyCredential implements HttpRequestInitializer {
    @Override
    public void initialize(HttpRequest request) throws IOException {

        HttpExecuteInterceptor handler = new HttpExecuteInterceptor() {
            @Override
            public void intercept(HttpRequest request) throws IOException {
                Log.d("status", request.getUrl() + "&key=AIzaSyCBj7L6BoILV-EnvrOcjFu2WvHuxTk44Co");
                request.setUrl(new GenericUrl(request.getUrl() + "&key=AIzaSyCBj7L6BoILV-EnvrOcjFu2WvHuxTk44Co"));
            }
        };
        request.setInterceptor(handler);

    }
}
