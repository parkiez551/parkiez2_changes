package com.parkiezmobility.parkiez.Interfaces;

import com.android.volley.VolleyError;

public interface APIResponseInterface<T> {
    <T> void OnSuccess(T response);
    void OnFailed(String response);
    void OnError(VolleyError error);
}
