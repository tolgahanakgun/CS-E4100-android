package fi.imagegrid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    private final static int SORT_BY_ASCENDING = 1;
    private final static int SORT_BY_DESCENDING = 2;
    private final static int SORT_BY_RECENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sortAscending(View view){
        GridUpdater gridUpdater =  new GridUpdater();
        gridUpdater.execute(new ImageGridView(SORT_BY_ASCENDING));
    }

    public void sortDescending(View view){
        GridUpdater gridUpdater =  new GridUpdater();
        gridUpdater.execute(new ImageGridView(SORT_BY_DESCENDING));
    }

    public void sortRecent(View view){
        GridUpdater gridUpdater =  new GridUpdater();
        gridUpdater.execute(new ImageGridView(SORT_BY_RECENT), new ImageGridView(1));
    }

    private Context getContext(){
        return this;
    }

    // Async task, fetches the data background and shows
    private class GridUpdater extends AsyncTask<ImageGridView, Void, ImageGridView>{
        @Override
        protected ImageGridView doInBackground(ImageGridView... view) {
            ImageGridView imageGridView = view[0];
            JSONImages jsonImages = new JSONImages(imageGridView.url);
            switch (imageGridView.sortBy){
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
            imageGridView.sampleGridViewAdapter = new GridViewAdapter(getContext(),jsonImages);
            return imageGridView;
        }

        @Override
        protected void onPostExecute(ImageGridView imageGridView) {
            imageGridView.view.setAdapter(imageGridView.sampleGridViewAdapter);
        }
    }

    // holds gridView and its adapter
    private class ImageGridView{
        private GridView view;
        private GridViewAdapter sampleGridViewAdapter = null;
        private String url;
        private int sortBy;
        ImageGridView(int sortBy){
            this.url = ((EditText)findViewById(R.id.txtUrl)).getText().toString();
            this.view = findViewById(R.id.gridView);
            this.sortBy = sortBy;
        }
    }
}