package com.anitha.gdriveintegration;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.File;

public class DriveServiceHelper {

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveService;

    public DriveServiceHelper(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }


    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
   /* public Task<String> createFile() {
        return Tasks.call(mExecutor, () -> {
            File metadata = new File()
                    .setParents(Collections.singletonList("root"))
                    .setMimeType("text/plain")
                    .setName("Untitled file");

            File googleFile = mDriveService.files().create(metadata).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        });
    }
*/
    public Task<String> createFile(String filepath) {


        return Tasks.call(mExecutor, () -> {

            File fileMetaData = new File();
            fileMetaData.setName("GDPInfoCSVFile");
            // fileMetaData.setName("MyCSVFile");

            java.io.File file = new java.io.File(filepath);
            FileContent mediaContent = new FileContent("text/csv", file);
            File myFile = null;
            try {
                myFile = mDriveService.files().create(fileMetaData, mediaContent).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (myFile == null) {
                throw new IOException("Null Result When Requesting");
            }
            return myFile.getId();


        });

    }


    /* public Task<Pair<String, String>> readFile(String fileId) {
         return Tasks.call(mExecutor, () -> {
             // Retrieve the metadata as a File object.
             File metadata = mDriveService.files().get(fileId).execute();
             String name = metadata.getName();

             // Stream the file contents to a String.
             try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                 StringBuilder stringBuilder = new StringBuilder();
                 String line;

                 while ((line = reader.readLine()) != null) {
                     stringBuilder.append(line);
                 }
                 String contents = stringBuilder.toString();

                 return Pair.create(name, contents);
             }
         });*/
    public Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");

        return intent;
    }

    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(
            ContentResolver contentResolver, Uri uri) {
        return Tasks.call(mExecutor, () -> {
            // Retrieve the document's display name from its metadata.
            String name;
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    name = cursor.getString(nameIndex);
                } else {
                    throw new IOException("Empty cursor returned for file.");
                }
            }

            // Read the document's contents as a String.
            String content;
            try (InputStream is = contentResolver.openInputStream(uri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                content = stringBuilder.toString();
            }

            return Pair.create(name, content);
        });
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<String> createFile() {
        return Tasks.call(mExecutor, () -> {
            File metadata = new File()
                    .setParents(Collections.singletonList("root"))
                    .setMimeType("text/csv")
                    .setName("PasswordCSVFile");

            File googleFile = mDriveService.files().create(metadata).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        });
    }

    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */
    public Task<Pair<String, String>> readFile(String fileId) {
        return Tasks.call(mExecutor, () -> {
            // Retrieve the metadata as a File object.
            File metadata = mDriveService.files().get(fileId).execute();
            String name = metadata.getName();

            // Stream the file contents to a String.
            try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String contents = stringBuilder.toString();

                return Pair.create(name, contents);
            }
        });
    }
}
