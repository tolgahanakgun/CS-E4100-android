package fi.imagegrid;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONImage {
    private URL url;
    private String author;
    private Date date;

    public JSONImage(String url, String author, String date) {
        try {
            this.url = new URL(url);
            this.author = author;
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            this.date = df1.parse(date);
        } catch (MalformedURLException e) {
            Log.e("@@@", "exception", e);
        } catch (ParseException e) {
            Log.e("@@@", "exception", e);
        }
    }

    public URL getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }
}
