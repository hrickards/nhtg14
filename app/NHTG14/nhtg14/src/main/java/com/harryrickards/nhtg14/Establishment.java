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
    public Integer rating;

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

    protected void parseData(JSONObject data) throws JSONException {
        establishmentName = data.getString("name");
        rating = data.getInt("score");
    }

    // Getter methods
    protected String getRatingString() {
        return Integer.toString(rating) + mContext.getString(R.string.rating_suffix);
    }
}
