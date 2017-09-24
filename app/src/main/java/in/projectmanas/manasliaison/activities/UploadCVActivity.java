package in.projectmanas.manasliaison.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.File;

import in.projectmanas.manasliaison.R;

public class UploadCVActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 007:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String filePath = null;
                    if (uri != null && "content".equals(uri.getScheme())) {
                        Cursor cursor = this.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        cursor.moveToFirst();
                        filePath = cursor.getString(0);
                        cursor.close();
                    } else {
                        filePath = uri.getPath();
                    }
                    File file = new File(filePath);
                    Log.d("", "Chosen path = " + filePath);
                    Backendless.Files.upload(file, "/myfiles", new UploadCallback() {
                        @Override
                        public void onProgressUpdate(Integer progress) {
                            //progress
                        }
                    }, new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {
                            Log.d("File uploaded", response.getFileURL());
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("fault", fault.toString());
                        }
                    });
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cv);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 007);
    }
}
