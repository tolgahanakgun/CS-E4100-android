package fi.imagegrid;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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
        //final int size = jsonImages.getImages().size();
        //Button b = findViewById(R.id.button);
        //b.setText(String.valueOf(size));
    }

    public void sortAscending(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editText = findViewById(R.id.txtUrl);
                String url = editText.getText().toString();
                JSONImages jsonImages = new JSONImages(url);
                jsonImages.sortByAuthorAscending();
                Button b = findViewById(R.id.sortAsc);
                b.setText(jsonImages.getImages().get(0).getAuthor());
            }
        });
    }

    public void sortDescending(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editText = findViewById(R.id.txtUrl);
                String url = editText.getText().toString();
                JSONImages jsonImages = new JSONImages(url);
                jsonImages.sortByAuthorDescending();
                Button b = findViewById(R.id.sortDes);
                b.setText(jsonImages.getImages().get(0).getAuthor());
            }
        });
    }

    public void sortRecent(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editText = findViewById(R.id.txtUrl);
                String url = editText.getText().toString();
                JSONImages jsonImages = new JSONImages(url);
                jsonImages.sortByDateRecentFirst();
                Button b = findViewById(R.id.sortDate);
                b.setText(jsonImages.getImages().get(0).getAuthor());
            }
        });
    }
}
