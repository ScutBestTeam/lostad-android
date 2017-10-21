package com.lostad.app.demo.network;

import com.android.volley.Response;

public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}

