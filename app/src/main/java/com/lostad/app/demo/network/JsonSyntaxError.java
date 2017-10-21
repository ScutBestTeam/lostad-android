package com.lostad.app.demo.network;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public class JsonSyntaxError extends VolleyError {
    public JsonSyntaxError() {
    }

    public JsonSyntaxError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public JsonSyntaxError(Throwable cause) {
        super(cause);
    }
}
