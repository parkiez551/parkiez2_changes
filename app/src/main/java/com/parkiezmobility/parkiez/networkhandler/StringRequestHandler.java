package com.parkiezmobility.parkiez.networkhandler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.parkiezmobility.parkiez.managers.ApplicationManager;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class StringRequestHandler extends StringRequest {
    private String body;

    public StringRequestHandler(int method, String url, String body_in, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.body = body_in;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String token = Utility.getPreference(ApplicationManager.getInstance().getContext(), Constants.TOKEN_PREF_KEY);
        if(token.compareToIgnoreCase("0") != 0) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return this.body == null ? null : this.body.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        /*String token = response.headers.get("token");
        if(token!=null){
            Utility.setPreference(ApplicationManager.getInstance().getContext(), Constants.PKEY_TOKEN,token);
        }*/
        return super.parseNetworkResponse(response);
    }
}
