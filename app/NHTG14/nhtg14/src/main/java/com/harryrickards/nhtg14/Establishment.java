package com.harryrickards.nhtg14;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Food establishment
 */
public class Establishment {
    public String establishmentName;
    public int rating;
    public String photoUrl;
    public int hygieneRating;
    public int structuralRating;
    public int managementRating;

    protected Context mContext;
    protected EstablishmentInterface mCallback;

    public Establishment(Location location, Context context) {
        mContext = context;
        mCallback = (EstablishmentInterface) context;

        getDetails(location);
    }

    public interface EstablishmentInterface {
        public void onEstablishmentDetailsFound();
    }

    protected void getDetails(Location location) {
        RequestParams params = new RequestParams();
        params.put("lat", Double.toString(location.getLatitude()));
        params.put("lng", Double.toString(location.getLongitude()));

        // TODO Send params
        APIClient.get("/ratings", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    parseData(data);
                    mCallback.onEstablishmentDetailsFound();
                } catch (JSONException e) {
                    Log.w("nhtg14", "failed" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable error) {
                Log.w("nhtg14", "failed" + error.getMessage());
            }
        });
    }

    // Get data out of the JSON response
    protected void parseData(JSONObject data) throws JSONException {
        establishmentName = data.getString("name");
        rating = data.getInt("score");

        JSONObject scores = data.getJSONObject("scores");
        hygieneRating = scores.getInt("hygiene");
        structuralRating = scores.getInt("structural");
        managementRating = scores.getInt("confidence_in_management");

        JSONArray photos = data.getJSONArray("photos");
        if (photos.length() > 0) {
            photoUrl = photos.getString(0);
        } else {
            photoUrl = null;
        }
    }
}
