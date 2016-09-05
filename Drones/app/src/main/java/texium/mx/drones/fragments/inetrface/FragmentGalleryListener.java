package texium.mx.drones.fragments.inetrface;

import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;

/**
 * Created by saurett on 05/02/2016.
 */
public interface FragmentGalleryListener {

    DecodeGallery updateDecodeGallery(DecodeGallery oldGallery);
    DecodeGallery getDecodeGallery();
    void setExtraDecodeGallery(TaskGallery taskGallery);
    void showQuestion(Integer idView);
    void openDescriptionFragment(String tag);
    void closeFragment(String tag);
    void replaceFragmentPhotoFragment();
    void replaceFragmentVideoFragment();



}
