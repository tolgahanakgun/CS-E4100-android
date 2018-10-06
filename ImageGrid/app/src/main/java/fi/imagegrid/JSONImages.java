package fi.imagegrid;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class JSONImages {
    private ArrayList<JSONImage> images;
    public JSONImages(String url){
        try {
            File jsonFile = File.createTempFile("images", "json", new File("/data/data/fi.imagegrid/cache/"));
            download(url,jsonFile);
            JSONArray jsonArray = parseJSONData(jsonFile);
            images = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                images.add(new JSONImage(jsonObject.getString("photo"), jsonObject.getString("author"), jsonObject.getString("date")));
            }
        } catch (IOException e){
            Log.e("@@@", "exception", e);
        } catch (JSONException e){
            Log.e("@@@", "exception", e);
        }
    }

    public ArrayList<JSONImage> getImages(){
        return images;
    }

    private void download(@NonNull String url, @NonNull File destFile) throws IOException {
        OkHttpClient client =  new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        BufferedSource source = body.source();

        BufferedSink sink = Okio.buffer(Okio.sink(destFile));
        Buffer sinkBuffer = sink.buffer();

        int bufferSize = 8 * 1024;
        for (long bytesRead; (bytesRead = source.read(sinkBuffer, bufferSize)) != -1; ) {
            sink.emit();
        }
        sink.flush();
        sink.close();
        source.close();
    }

    private JSONArray parseJSONData(File file) {
        String JSONString;
        JSONArray JSONArray = null;
        try {

            //open the inputStream to the file
            InputStream inputStream = new FileInputStream(file);

            int sizeOfJSONFile = inputStream.available();

            //array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            //reading data into the array from the file
            inputStream.read(bytes);

            //close the input stream
            inputStream.close();

            JSONString = new String(bytes, "UTF-8");
            JSONArray = new JSONArray(JSONString);

        } catch (IOException e) {
            Log.e("@@@", "exception", e);
        }
        catch (JSONException e) {
            Log.e("@@@", "exception", e);
        }
        return JSONArray;
    }

    private class AuthorDescendingComparator implements java.util.Comparator<JSONImage> {

        @Override
        public int compare(JSONImage image1, JSONImage image2) {
            return image2.getAuthor().compareTo(image1.getAuthor());
        }
    }
    private class AuthorAscendingComparator implements Comparator<JSONImage>{

        @Override
        public int compare(JSONImage image1, JSONImage image2) {
            return image1.getAuthor().compareTo(image2.getAuthor());
        }
    }
    private class DateComparator implements Comparator<JSONImage>{

        @Override
        public int compare(JSONImage image1, JSONImage image2) {
            return image1.getDate().compareTo(image2.getDate());
        }
    }

    public void sortByAuthorAscending(){
        Collections.sort(this.images, new AuthorAscendingComparator());
    }

    public void sortByAuthorDescending(){
        Collections.sort(this.images, new AuthorDescendingComparator());
    }

    public void sortByDateRecentFirst(){
        Collections.sort(this.images, new DateComparator());
    }
}
