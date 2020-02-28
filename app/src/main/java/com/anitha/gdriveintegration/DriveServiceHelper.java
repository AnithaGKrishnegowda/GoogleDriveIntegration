package com.anitha.gdriveintegration;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.File;

public class DriveServiceHelper {

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveService;

    public DriveServiceHelper(Drive mDriveService)
    {
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

    public Task<String> createFilePDF(String filepath) {


            return Tasks.call(mExecutor, () -> {

                File fileMetaData = new File();
                fileMetaData.setName("ExcelExampleFile");
               // fileMetaData.setName("MyCSVFile");

                java.io.File file = new java.io.File(filepath);
                FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", file);
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


}
