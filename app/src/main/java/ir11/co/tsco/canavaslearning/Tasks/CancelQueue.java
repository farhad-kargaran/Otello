package ir11.co.tsco.canavaslearning.Tasks;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir11.co.tsco.canavaslearning.utils.AppConstant;
import ir11.co.tsco.canavaslearning.utils.ResponseModel;
import ir11.co.tsco.canavaslearning.utils.ServiceHandler;

/**
 * Created by Farhad on 5/10/2016.
 */
public class CancelQueue {
    private final static String Url = AppConstant.gameWebServiceUrl + "/queues/";

    public CancelQueue(final String queue_id)
    {

        ServiceHandler sh = new ServiceHandler();


        String newUrl = Url + queue_id + ".json";


        Map<String, String> params;
        params = new HashMap<>();
        params.put("id",queue_id);

        sh.makeServiceCall(newUrl, Request.Method.DELETE, params, new ServiceHandler.MyCallBack()
        {
            @Override
            public void Success(ResponseModel message)
            {
                try {
                    JSONObject jsonResp = new JSONObject(message.response);

                    String statusCode = jsonResp.getString("status");
                    String msg = jsonResp.getString("message");
                    String _data =  jsonResp.getString("data");
                    int code = jsonResp.getInt("code");
                    if(code == 204)
                    {
                        Log.i("otot","successfuly removed from queue ");
                    }
                    else
                        Log.i("otot","error number" + code + " while trying to remove player from the queue");

                }

                catch (Exception ee)
                {
                    Log.i("otot","error removing from = " + ee.getMessage());
                }

            }

            @Override
            public void Failed(ResponseModel message)
            {

            }
        });

    }
}
