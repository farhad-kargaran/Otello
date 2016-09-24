package ir11.co.tsco.canavaslearning.Tasks;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir11.co.tsco.canavaslearning.MainActivity;
import ir11.co.tsco.canavaslearning.utils.AppConstant;
import ir11.co.tsco.canavaslearning.utils.ResponseModel;
import ir11.co.tsco.canavaslearning.utils.ServiceHandler;

/**
 * Created by Farhad on 5/10/2016.
 */
public class CreateUser {

    private final static String Url = AppConstant.gameWebServiceUrl + "/users.json";

    public CreateUser(final String identifier, final String name)
    {

        ServiceHandler sh = new ServiceHandler();


        Map<String, String> params;
        params = new HashMap<>();
        params.put("identifier",identifier);
        params.put("client_project","gapotello");
        params.put("name",name);



        sh.makeServiceCall(Url, Request.Method.POST, params, new ServiceHandler.MyCallBack()
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
                    if(code == 201)
                    {
                        JSONObject data = new JSONObject(_data);
                        MainActivity._id = data.getString("_id");
                        Log.i("otot","user created with id = " + MainActivity._id);
                        MainActivity.sGame.getStatus(MainActivity._id);
                    }
                }

                catch (Exception ee)
                {

                }
                //JSONObject jsonResp =new JSONObject(responseString);
            }

            @Override
            public void Failed(ResponseModel message)
            {

            }
        });

    }
}
