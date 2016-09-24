package ir11.co.tsco.canavaslearning.Tasks;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import ir11.co.tsco.canavaslearning.MainActivity;
import ir11.co.tsco.canavaslearning.utils.AppConstant;
import ir11.co.tsco.canavaslearning.utils.Helper;
import ir11.co.tsco.canavaslearning.utils.PrefHandler;
import ir11.co.tsco.canavaslearning.utils.ResponseModel;
import ir11.co.tsco.canavaslearning.utils.ServiceHandler;

/**
 * Created by Farhad on 5/10/2016.
 */
public class AddToQueue {

    private final static String Url = AppConstant.gameWebServiceUrl + "/queues.json";

    public AddToQueue(final String user_id, final String game, final  String user_level)
    {

        ServiceHandler sh = new ServiceHandler();


        Map<String, String> params;
        params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("game",game);
        params.put("user_level",user_level);
        params.put("ip", Helper.getIPAddress(true));


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
                        MainActivity.queue_id = data.getString("_id");
                        PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_LAST_QUEUE_ID,MainActivity.queue_id);
                        Log.i("otot","added to queue with id = " + MainActivity.queue_id + " , id saved in preferences too.");


                        try {

                            IO.Options opts = new IO.Options();
                            opts.forceNew = true;
                            opts.query = "user_id=" + MainActivity._id;
                            MainActivity.socket = IO.socket(AppConstant.handshakeUrl,opts);


                            MainActivity.socket.connect();
                            MainActivity.socket.on("start", new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {

                                    Object[] object = args;
                                    JSONObject obj = (JSONObject) object[0];
                                    String id;
                                    try {
                                        id = obj.getString("id");
                                        MainActivity.game_id = id;
                                        Log.e("otot","socket io ack received! gameId is = " + MainActivity.game_id);
                                        MainActivity.sGame.getDetail(id);


                                    }
                                    catch (Exception ee)
                                    {

                                    }

                                    //Ack ack = (Ack) args[args.length - 1];
                                    //ack.call();
                                }
                            });
                        }
                        catch (Exception ee)
                        {

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
                Log.i("otot","error adding to queue" + "\n");

            }
        });

    }
}
