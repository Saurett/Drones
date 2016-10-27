package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.fragments.PreviewMemberFragment;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;

/**
 * Created by saurett on 14/01/2016.
 */
public class PreviewMemberAdapter extends RecyclerView.Adapter<PreviewMemberAdapter.ViewHolder>{

    View.OnClickListener onClickListener;
    List<TaskGallery> preview_member_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_picture;
        TextView item_member_name;
        TextView item_member_job;
        Button delete_button;

        public ViewHolder(View itemView) {
            super(itemView);

            item_picture = (ImageView) itemView.findViewById(R.id.item_preview_member_photo);
            item_member_name = (TextView) itemView.findViewById(R.id.item_preview_member_name);
            item_member_job = (TextView) itemView.findViewById(R.id.item_preview_member_job);
            delete_button = (Button) itemView.findViewById(R.id.item_preview_member_delete);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TaskGallery getItemByPosition(int position) { return preview_member_list.get(position);}

    public void addAll(List<TaskGallery> photos_list) { this.preview_member_list.addAll(photos_list);}

    public void remove(int position) { this.preview_member_list.remove(position);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_member_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TaskGallery item = preview_member_list.get(position);

        final DecodeGallery decodeGallery = new DecodeGallery();

        decodeGallery.setTaskGallery(item);

        holder.item_member_name.setText(item.getMember_name());
        holder.item_member_job.setText(item.getMember_job());
        holder.item_picture.setImageBitmap(item.getPhoto_bitmap());

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeGallery.setIdView(v.getId());
                decodeGallery.setPosition(position);
                PreviewMemberFragment.showQuestion(decodeGallery);
            }
        });
    }

    @Override
    public int getItemCount() {
        return preview_member_list == null ? 0 : preview_member_list.size();
    }

    public void removeItem(int position) {
        this.preview_member_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

}
