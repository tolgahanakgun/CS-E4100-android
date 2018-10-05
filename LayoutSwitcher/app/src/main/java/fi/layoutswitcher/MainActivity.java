package fi.layoutswitcher;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_portrait);
        MainActivity.context = getApplicationContext();
        final Switch onOffSwitch = findViewById(R.id.scrChange);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onOffSwitch.setTextOn(Boolean.toString(isChecked));
                if(isChecked) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    TextView textLandscape = findViewById(R.id.textLandscape);
                    textLandscape.setVisibility(View.VISIBLE);
                    TextView textPortrait = findViewById(R.id.textPortrait);
                    textPortrait.setVisibility(View.INVISIBLE);
                    ImageView iv = findViewById(R.id.imageView);
                    Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.butter_fly);
                    Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 100, 100, true);
                    iv.setImageBitmap(bMapScaled);
                    iv.getLayoutParams().height = 100;
                    iv.getLayoutParams().width = 100;
                }
                else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    TextView textLandscape = findViewById(R.id.textLandscape);
                    textLandscape.setVisibility(View.INVISIBLE);
                    TextView textPortrait = findViewById(R.id.textPortrait);
                    textPortrait.setVisibility(View.VISIBLE);
                    ImageView iv = findViewById(R.id.imageView);
                    Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.butter_fly);
                    Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 350, 650, true);
                    iv.setImageBitmap(bMapScaled);
                    iv.getLayoutParams().height = 650;
                    iv.getLayoutParams().width = 350;
                }
            }
        });
    }
}
