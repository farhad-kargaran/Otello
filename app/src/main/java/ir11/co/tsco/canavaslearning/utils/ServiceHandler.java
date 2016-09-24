package ir11.co.tsco.canavaslearning.utils;

import android.os.SystemClock;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir11.co.tsco.canavaslearning.App;

/**
 * Created by Babax on 9/23/2015.
 */
public class ServiceHandler {

    private final static String HeaderParamToken = "X-Token";
    //private final String USER_AGENT = "http.useragent";

    private static final int SOCKET_TIMEOUT_MS = 120000;
    private static final int MAX_RETRIES = 2;

    //    public final static int GET = 1;
    //    public final static int POST = 2;
    //    public final static int DELETE = 3;
    //    public final static int PUT = 4;

    private RequestQueue requestQueue = null;
    //    public long mediaId;

    public interface MyCallBack {
        void Success(ResponseModel message);

        void Failed(ResponseModel message);
    }

    public interface MultiPartCallback {
        void Success(ResponseModel message);

        void Failed(ResponseModel message);

        void Transferred(long url, long transfered, int progress);
    }

    public ServiceHandler() {
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    public void makeServiceCall(final String url, int method, final MyCallBack callback) {
        makeServiceCall(url, method, new HashMap<String, String>(), callback);
    }

    int lastProgress = 0;

    public void makeMultiPartServiceCall(final long mediaId, final String url, final Map<String, String> params, final MultiPartCallback callback)//method is POST
    {
        final ResponseModel responseModel = new ResponseModel();
        android.util.Log.e(AppConstant.TAG, "makeMultiPartServiceCall");

        if (Helper.isNetworkAvailable()) {
            for (Map.Entry<String, String> row : params.entrySet()) {
                String key = row.getKey();
                String value = row.getValue();

                File file = new File(value);
                final MultipartRequest multiPartRequest = new MultipartRequest(url, file, null, params, key, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.e(AppConstant.TAG + " multipart url", url);
                        android.util.Log.e(AppConstant.TAG + " multipart err", error.toString() + " | " + error.getMessage());

                        responseModel.setException(error);

                        if (error != null) {
                            NetworkResponse errNR = error.networkResponse;

                            if (errNR != null) {
                                if (errNR.data != null) {
                                    responseModel.setResponse(new String(errNR.data));
                                    android.util.Log.e(AppConstant.TAG + " multipart err", new String(errNR.data));
                                }

                                responseModel.setStatusCode(errNR.statusCode);
                            }
                        }

                        callback.Failed(responseModel);

                    }
                }, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        android.util.Log.d(AppConstant.TAG + " multipart url", url);
                        android.util.Log.d(AppConstant.TAG + " multipart ", response);
                        responseModel.setResponse(response);
                        callback.Success(responseModel);
                    }
                }, new MultipartRequest.MultipartProgressListener() {
                    @Override
                    public void transferred(long transfered, int progress) {
                        if ( /*!AppConstant.CANCEL_LIST_IMAGE.contains(mediaId) &&*/ progress > lastProgress && lastProgress < 100) {
                            lastProgress = progress;
                            callback.Transferred(mediaId, transfered, progress);
                            SystemClock.sleep(200);
                        }

                        android.util.Log.e(AppConstant.TAG, "mediaId :" + mediaId + "  progress : " + progress + "  transferred : " + transfered + " in thread :" + Thread.currentThread().getName());

                    }
                });

                multiPartRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_MS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                multiPartRequest.setTag(mediaId);

                requestQueue.add(multiPartRequest);

            }
        }
    }

    public void makeServiceCall(final String url, int method, final Map<String, String> params, final MyCallBack callback) {

        final ResponseModel responseModel = new ResponseModel();
        if (Helper.isNetworkAvailable()) {

            StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    android.util.Log.d(AppConstant.TAG + " ServiceHandler url", url);
                    android.util.Log.d(AppConstant.TAG + " ServiceHandler ", response);
                    if (response != null) {
                        responseModel.setResponse(response);
                        callback.Success(responseModel);
                    } else {
                        android.util.Log.d(AppConstant.TAG + " ServiceHandler ->", "resoponse was null");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    android.util.Log.e(AppConstant.TAG + " ServiceHandler url", url);
                    android.util.Log.e(AppConstant.TAG + " ServiceHandler err", error.toString());

                    responseModel.setException(error);

                    if (error != null) {
                        NetworkResponse errNR = error.networkResponse;

                        if (errNR != null) {


                          if (errNR.data != null) {
                                android.util.Log.e(AppConstant.TAG + " ServiceHandler err", new String(errNR.data));
                                responseModel.setResponse(new String(errNR.data));
                            }


                            responseModel.setStatusCode(errNR.statusCode);
                        }
                    }

                    callback.Failed(responseModel);
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {

                    params.put("Content-Type", "application/x-www-form-urlencoded");

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        responseModel.setStatusCode(response.statusCode);
                        android.util.Log.d(AppConstant.TAG + " StatusCode", String.valueOf(response.statusCode));
                    }

                    return super.parseNetworkResponse(response);
                }

                /*@Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }*/

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("Accept", "application/json");
                    params.put(HeaderParamToken, PrefHandlerSec.getInstance().getString(PrefHandlerSec.PROPERTY_TOKEN, Helper.getIMEI_MD5(App.getInstance(), "")));
                    params.put("APPVERSION", String.valueOf(Helper.getAppVersionCode()));
                    params.put("user-agent", System.getProperty("http.agent"));

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_MS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);

        } else {
            responseModel.setStatusCode(264);
            responseModel.setException(new Exception("No Internet Access"));
            callback.Failed(responseModel);
        }
    }

    public ResponseModel makeSynchronousServiceCall(final String url, int method, final Map<String, String> params) {

        final ResponseModel responseModel = new ResponseModel();
        if (Helper.isNetworkAvailable()) {

            RequestFuture<String> future = RequestFuture.newFuture();

            StringRequest stringRequest2 = new StringRequest(method, url, future, future) {

                protected Map<String, String> getParams() throws AuthFailureError {
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("Accept", "application/json");
                    params.put(HeaderParamToken, PrefHandlerSec.getInstance().getString(PrefHandlerSec.PROPERTY_TOKEN, Helper.getIMEI_MD5(App.getInstance(), "")));

                    params.put("APPVERSION", String.valueOf(Helper.getAppVersionCode()));
                    params.put("http.useragent", System.getProperty("http.agent"));

                    return params;
                }

            };

            stringRequest2.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_MS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            );

            requestQueue.add(stringRequest2);

            try {
                String response = future.get();
                android.util.Log.d(AppConstant.TAG + " Synchronous url", url);
                android.util.Log.d(AppConstant.TAG + " Synchronous ", response);
                if (response != null) {
                    responseModel.setResponse(response);
                    return responseModel;
                } else {
                    android.util.Log.d(AppConstant.TAG + " ServiceHandler ->", "resoponse was null");
                }

            } catch (Exception error) {
                responseModel.setException(error);
                Helper.showErrorMessage("ServiceHandler Synchronous", error);
            }

            return null;

        } else {
            responseModel.setException(new Exception("No Internet Access"));
            return null;
        }
    }



}
