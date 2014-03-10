package com.harryrickards.nhtg14;


import android.app.Fragment;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    TextView reviewLabel;
    TextView reviewText;
    ImageView reviewRating;
    TextView overallRatingLabel;
    TextView hygieneRatingLabel;
    TextView structuralRatingLabel;
    TextView managementRatingLabel;
    int MIN_PHOTO_WIDTH = 300;

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
        overallRatingLabel = (TextView) view.findViewById(R.id.overallFhrLabel);
        hygieneRatingLabel = (TextView) view.findViewById(R.id.hygieneLabel);
        managementRatingLabel = (TextView) view.findViewById(R.id.managementLabel);
        structuralRatingLabel = (TextView) view.findViewById(R.id.structuralLabel);
        reviewLabel = (TextView) view.findViewById(R.id.reviewLabel);
        reviewText = (TextView) view.findViewById(R.id.reviewText);
        reviewRating = (ImageView) view.findViewById(R.id.reviewRating);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MIN_PHOTO_WIDTH = size.x - 100;
    }

    // Show data from an API response
    public void showEstablishment(Establishment establishment) {
        name.setText(establishment.establishmentName);

        UrlImageViewHelper.setUrlDrawable(photo, establishment.photoUrl, R.drawable.placeholder);
        if (photo.getLayoutParams().height < MIN_PHOTO_WIDTH) {
            photo.getLayoutParams().height = MIN_PHOTO_WIDTH;
        }

        setImageViewFromRating(overallRating, establishment.rating, overallRatingLabel);
        setImageViewFromRating(hygieneRating, establishment.hygieneRating, hygieneRatingLabel);
        setImageViewFromRating(structuralRating, establishment.structuralRating, structuralRatingLabel);
        setImageViewFromRating(managementRating, establishment.managementRating, managementRatingLabel);

        // If establishment hasn't got a review, hide the fields
        if (establishment.reviewText == null) {
            reviewText.setVisibility(View.GONE);
            reviewLabel.setVisibility(View.GONE);
            reviewRating.setVisibility(View.GONE);
        } else {
            reviewText.setVisibility(View.VISIBLE);
            reviewLabel.setVisibility(View.VISIBLE);
            reviewRating.setVisibility(View.VISIBLE);

            reviewText.setText(establishment.reviewText);
            setImageViewFromRating(reviewRating, establishment.reviewRating, reviewLabel);
        }
    }

    protected void setImageViewFromRating(ImageView imageView, int rating, TextView label) {
        // TODO Check for null not zero
        if (rating == 0) {
            imageView.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            imageView.setImageResource(drawableFromRating(rating));
        }
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
