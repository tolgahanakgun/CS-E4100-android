package fi.imagegrid;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final static int SORT_BY_ASCENDING = 1;
    private final static int SORT_BY_DESCENDING = 2;
    private final static int SORT_BY_RECENT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void showCount(View view){
        JSONImages jsonImages = new JSONImages("https://api.myjson.com/bins/s7200");
        TextView vi = findViewById(R.id.textView2);
        vi.setText("");
        for(int index =0; index <jsonImages.getImages().size();index++){
            vi.setText(vi.getText()+"\t"+String.valueOf(index)+" - "+((JSONImage)jsonImages.getImages().get(index)).getAuthor()+"\n");
        }
    }

    public void sortAscending(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortAndShow(SORT_BY_ASCENDING);
            }
        });
    }

    public void sortDescending(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortAndShow(SORT_BY_DESCENDING);
            }
        });
    }

    public void sortRecent(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortAndShow(SORT_BY_RECENT);
            }
        });
    }

    private void sortAndShow(int sortBy){
        EditText editText = findViewById(R.id.txtUrl);
        String url = editText.getText().toString();
        JSONImages jsonImages = new JSONImages(url);
        switch (sortBy){
            case SORT_BY_ASCENDING:
                jsonImages.sortByAuthorAscending();
                break;
            case SORT_BY_DESCENDING:
                jsonImages.sortByAuthorDescending();
                break;
            case SORT_BY_RECENT:
                jsonImages.sortByDateRecentFirst();
                break;
        }
        String[] urls = new String[jsonImages.getImages().size()];
        for(int index=0;index<jsonImages.getImages().size();index++){
            urls[index] = jsonImages.getImages().get(index).getUrl().toString();
        }
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new SampleGridViewAdapter(getContext(),urls));
    }

    private Context getContext(){
        return this;
    }
}
