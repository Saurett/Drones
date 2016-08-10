package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.PhotoGallery;

/**
 * Created by saurett on 14/01/2016.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder>{

    /* En caso de llamar una actividad desde el adapter
    Context context = v.getContext();
    Intent intent = new Intent(context, MainActivity.class);
    context.startActivity(intent);
    */

    View.OnClickListener onClickListener;
    List<PhotoGallery> photo_gallery_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText description_picture;
        Button cloud_picture_button;
        Button delete_picture_button;

        public ViewHolder(View itemView) {
            super(itemView);

            description_picture = (EditText) itemView.findViewById(R.id.photoDescription);
            cloud_picture_button = (Button) itemView.findViewById(R.id.cloudPhoto);
            delete_picture_button = (Button) itemView.findViewById(R.id.deletePhoto);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public PhotoGallery getItemByPosition(int position) { return photo_gallery_list.get(position);}

    public void addAll(List<PhotoGallery> photos_list) { this.photo_gallery_list.addAll(photos_list);}

    public void remove(int position) { this.photo_gallery_list.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PhotoGallery item = photo_gallery_list.get(position);

        holder.description_picture.setText(item.getDescription());

        /*

        holder.agree_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJumper(v,task,taskDecode);
            }
        });

        holder.finish_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fragmentJumper(v,task,taskDecode);
            }
        });


        holder.decline_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJumper(v,task,taskDecode);
            }
        });

        holder.gallery_task_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentJumper(v,task,taskDecode);
            }
        });

        */
    }

    @Override
    public int getItemCount() {
        return photo_gallery_list == null ? 0 : photo_gallery_list.size();
    }

}
