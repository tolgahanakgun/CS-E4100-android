package fi.imagegrid;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class GridViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<JSONImage> images;

    public GridViewAdapter(Context context, JSONImages images) {
        this.context = context;
        this.images = images.getImages();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View container =  convertView;
        if (container== null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            container = inflater.inflate(R.layout.image_container, null);
        }

        // Get the image URL for the current position.
        String url = getItem(position);
        ImageView imgView =  container.findViewById(R.id.image);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.holder)
                .error(R.drawable.error)
                .centerCrop()
                .resize(100,70)
                .tag(context)
                .into(imgView);

        TextView author = container.findViewById(R.id.author);
        author.setText(getTitle(position));
        return container;
        }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public String getItem(int position) {
        return images.get(position).getUrl().toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String getTitle(int position){
        return images.get(position).getAuthor();
    }
}