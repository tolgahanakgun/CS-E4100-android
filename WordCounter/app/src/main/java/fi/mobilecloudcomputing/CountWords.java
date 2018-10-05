package fi.mobilecloudcomputing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CountWords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_words);
    }

    public void countWords(View view) {
        EditText textInput = findViewById(R.id.textInput);
        TextView textOutput = findViewById(R.id.txtCount);
        textOutput.setText("Word Count is: " + String.valueOf(countWords(textInput.getText().toString())));
    }

    private int countWords(String string){
        String trim = string.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
    }
}
