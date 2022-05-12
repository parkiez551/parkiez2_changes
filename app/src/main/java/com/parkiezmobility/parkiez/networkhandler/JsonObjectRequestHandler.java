package com.parkiezmobility.parkiez.networkhandler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parkiezmobility.parkiez.managers.ApplicationManager;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class JsonObjectRequestHandler extends JsonObjectRequest {
    private JSONObject body;

    public JsonObjectRequestHandler(int method, String url, JSONObject body_in, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, body_in, listener, errorListener);
        this.body = body_in;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
       // headers.put("User-Agent", "Mozilla/5.0");
        String token = Utility.getPreference(ApplicationManager.getInstance().getContext(), Constants.TOKEN_PREF_KEY);
        if(token.compareToIgnoreCase("0") != 0) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }
}
