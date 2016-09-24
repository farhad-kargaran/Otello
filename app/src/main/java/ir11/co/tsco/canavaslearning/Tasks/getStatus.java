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
public class getStatus {

    private final static String Url = AppConstant.gameWebServiceUrl + "/users/status/";

    public getStatus(final String user_id,final boolean addToQueue)
    {


        String newUrl = Url + user_id + ".json";
        ServiceHandler sh = new ServiceHandler();


        Map<String, String> params;
        params = new HashMap<>();
        params.put("user_id",user_id);


        sh.makeServiceCall(newUrl, Request.Method.GET, params, new ServiceHandler.MyCallBack()
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
                    if(code == 200)
                    {
                        JSONObject data = new JSONObject(_data);
                        String type = data.getString("type"); //inQueue || playing

                        if(type.equalsIgnoreCase("inQueue")) {
                            Log.i("otot","player type in getStatus.java is inQueue ");
                            String _queue = data.getString("queue");
                            JSONObject queue = new JSONObject(_queue);
                            String _id = queue.getString("_id");
                            String time = queue.getString("time");
                            String ip = queue.getString("ip");
                            String user_level = queue.getString("user_level");
                            String user_id = queue.getString("user_id");
                            String project_id = queue.getString("project_id");
                            Log.i("otot","inQueue = projectId = " + project_id);
                            //MainActivity.game_id = project_id;
                        }
                        if(type.equalsIgnoreCase("playing")) {
                            Log.i("otot","player type in getStatus.java is playing ");
                            String _game = data.getString("game");
                            JSONObject game = new JSONObject(_game);
                            String _id = game.getString("_id");

                        }
                        if(type.equalsIgnoreCase("silent")) {
                            Log.i("otot","player type in getStatus.java is silent ");

                            if(addToQueue)
                            {
                                MainActivity.sGame.addQueue(MainActivity._id);
                            }


                        }


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
                Log.i("otot","error getting status" + "\n");

            }
        });

    }
}
