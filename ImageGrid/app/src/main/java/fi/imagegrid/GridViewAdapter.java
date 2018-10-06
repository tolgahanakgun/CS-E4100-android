package fi.imagegrid;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class SampleGridViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> urls = new ArrayList<>();

    SampleGridViewAdapter(Context context, String[] urlArray) {
        this.context = context;
        Collections.addAll(urls, urlArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View container = (LinearLayout) convertView;
        if (container== null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            container = inflater.inflate(R.layout.image_container, null);
        }

        // Get the image URL for the current position.
        String url = getItem(position);
        SquaredImageView squaredView = null;
        ImageView imgView = (ImageView) container.findViewById(R.id.image);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.get() //
                .load(url) //
                .placeholder(R.drawable.holder) //
                .error(R.drawable.error) //
                //.fit() //
                .centerCrop()
                .resize(100,70)
                .tag(context) //
                .into(imgView);

       // imgView = squaredView;
        TextView author = (TextView) container.findViewById(R.id.author);
        author.setText("Test Author");
        return container;

        }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}