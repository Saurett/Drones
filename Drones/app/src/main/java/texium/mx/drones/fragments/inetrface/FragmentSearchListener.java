package texium.mx.drones.fragments.inetrface;

import java.util.List;

import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;

/**
 * Created by saurett on 05/02/2016.
 */
public interface FragmentSearchListener {

    void updateProfileSearch(String query);
    String getProfileSearch ();

    void closeFragment(String tag);
    void replaceFragmentSearchFragment();
    void replaceFragmentPreviewFragment();
    DecodeGallery updateDecodeGallery(DecodeGallery oldGallery);
    DecodeGallery getDecodeGallery();
    void showQuestion(Integer idView);
    List<TaskGallery> getPreviewMembers();
    List<TaskGallery> updatePreviewMembers(List<TaskGallery> oldGallery);



/*
    DecodeGallery updateDecodeGallery(DecodeGallery oldGallery);
    DecodeGallery getDecodeGallery();
    void setExtraDecodeGallery(TaskGallery taskGallery);
    void showQuestion(Integer idView);
    void openDescriptionFragment(String tag);
    void closeFragment(String tag);
    void replaceFragmentPhotoFragment();
    void replaceFragmentVideoFragment();
    void replaceFragmentDocumentFragment();
    void replaceFragmentMemberFragment();
    void setEmptyDescription(int size);
    Boolean openGallery();
    */



}
