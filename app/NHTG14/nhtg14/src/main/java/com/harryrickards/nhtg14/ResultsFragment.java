package com.harryrickards.nhtg14;


import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ResultsFragment extends Fragment {
    // Data views
    TextView name;
    ImageView photo;
    ImageView overallRating;
    ImageView hygieneRating;
    ImageView structuralRating;
    ImageView managementRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Find views
        name = (TextView) view.findViewById(R.id.name);
        photo = (ImageView) view.findViewById(R.id.picture);
        overallRating = (ImageView) view.findViewById(R.id.overallRating);
        hygieneRating = (ImageView) view.findViewById(R.id.hygieneRating);
        structuralRating = (ImageView) view.findViewById(R.id.structuralRating);
        managementRating = (ImageView) view.findViewById(R.id.managementRating);
    }

    // Show data from an API response
    public void showEstablishment(Establishment establishment) {
        name.setText(establishment.establishmentName);
        UrlImageViewHelper.setUrlDrawable(photo, establishment.photoUrl, R.drawable.placeholder);
        setImageViewFromRating(overallRating, establishment.rating);
        setImageViewFromRating(hygieneRating, establishment.hygieneRating);
        setImageViewFromRating(structuralRating, establishment.structuralRating);
        setImageViewFromRating(managementRating, establishment.managementRating);
    }

    protected void setImageViewFromRating(ImageView imageView, int rating) {
        imageView.setImageResource(drawableFromRating(rating));
    }

    protected int drawableFromRating(int rating) {
        int drawable;
        switch (rating) {
            case 5:
                drawable = R.drawable.star_5;
                break;

            case 4:
                drawable = R.drawable.star_4;
                break;

            case 3:
                drawable = R.drawable.star_3;
                break;

            case 2:
                drawable = R.drawable.star_2;
                break;

            case 1:
                drawable = R.drawable.star_1;
                break;

            default:
                drawable = R.drawable.star_0;
                break;
        }

        return drawable;
    }
}
