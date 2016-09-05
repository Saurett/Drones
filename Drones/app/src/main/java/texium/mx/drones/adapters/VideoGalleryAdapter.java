package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.fragments.VideoGalleryFragment;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.utils.Constants;

/**
 * Created by saurett on 14/01/2016.
 */
public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<TaskGallery> video_gallery_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_video;
        Button sync_button;
        Button description_button;
        Button delete_button;

        public ViewHolder(View itemView) {
            super(itemView);

            item_video = (ImageView) itemView.findViewById(R.id.item_video);
            sync_button = (Button) itemView.findViewById(R.id.item_video_sync);
            description_button = (Button) itemView.findViewById(R.id.item_video_description);
            delete_button = (Button) itemView.findViewById(R.id.item_video_delete);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TaskGallery getItemByPosition(int position) { return video_gallery_list.get(position);}

    public void addAll(List<TaskGallery> photos_list) { this.video_gallery_list.addAll(photos_list);}

    public void remove(int position) { this.video_gallery_list.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TaskGallery item = video_gallery_list.get(position);

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
            case Constants.ITEM_SYNC_SERVER_CLOUD_OFF:
                resource = R.mipmap.ic_cloud_off_black;
                break;
            default:
                resource = android.R.drawable.ic_menu_delete;
                break;

        }

        final DecodeGallery decodeGallery = new DecodeGallery();

        decodeGallery.setTaskGallery(item);

        holder.sync_button.setBackgroundResource(resource);
        holder.item_video.setImageBitmap(item.getPhoto_bitmap());

        holder.sync_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeGallery.setIdView(v.getId());
                decodeGallery.setPosition(position);
                VideoGalleryFragment.showQuestion(decodeGallery);
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeGallery.setIdView(v.getId());
                decodeGallery.setPosition(position);
                VideoGalleryFragment.showQuestion(decodeGallery);
            }
        });

        holder.description_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeGallery.setIdView(v.getId());
                decodeGallery.setPosition(position);
                VideoGalleryFragment.showQuestion(decodeGallery);
            }
        });
    }

    @Override
    public int getItemCount() {
        return video_gallery_list == null ? 0 : video_gallery_list.size();
    }

    public void removeItem(int position) {
        this.video_gallery_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
