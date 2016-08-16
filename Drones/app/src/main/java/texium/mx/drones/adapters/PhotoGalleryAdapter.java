package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.PhotoGallery;
import texium.mx.drones.utils.Constants;

/**
 * Created by saurett on 14/01/2016.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<PhotoGallery> photo_gallery_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button sync_button;
        Button description_button;
        Button delete_button;

        public ViewHolder(View itemView) {
            super(itemView);

            sync_button = (Button) itemView.findViewById(R.id.item_photo_sync);
            description_button = (Button) itemView.findViewById(R.id.item_photo_description);
            delete_button = (Button) itemView.findViewById(R.id.item_photo_delete);
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PhotoGallery item = photo_gallery_list.get(position);

        int syncType = item.getSync_type().intValue();

        int resource;
        switch (syncType) {
            case Constants.ITEM_SYNC_SERVER_DEFAULT:
                resource = R.mipmap.ic_computer_black;
                break;
            case Constants.ITEM_SYNC_LOCAL_TABLET:
                resource = R.mipmap.ic_tablet_android_black;
                break;
            case Constants.ITEM_SYNC_SERVER_CLOUD:
                resource = R.mipmap.ic_cloud_black;
                break;
            default:
                resource = R.drawable.logo_df_civar;
                break;

        }

        holder.sync_button.setBackgroundResource(resource);
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        /*
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

    public void removeItem(int position) {
        this.photo_gallery_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
