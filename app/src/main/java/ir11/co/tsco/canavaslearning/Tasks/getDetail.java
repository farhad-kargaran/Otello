package ir11.co.tsco.canavaslearning.Tasks;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir11.co.tsco.canavaslearning.utils.AppConstant;
import ir11.co.tsco.canavaslearning.utils.ResponseModel;
import ir11.co.tsco.canavaslearning.utils.ServiceHandler;

/**
 * Created by Farhad on 5/10/2016.
 */
public class getDetail {

    private final static String Url = AppConstant.gameWebServiceUrl + "/games/";

    public getDetail(final String game_id)
    {
        String newUrl = Url + game_id + ".json";
        ServiceHandler sh = new ServiceHandler();

        Map<String, String> params;
        params = new HashMap<>();
        params.put("id",game_id);


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
                        String _id = data.getString("_id");
                        String address = data.getString("address"); // example = n0.brainteaser.appdana.com
                        String start_time = data.getString("start_time"); // example = 1463213653
                        String remaining_time = data.getString("remaining_time"); //example = 90
                        String _message = data.getString("message"); //example = null
                        String project_id = data.getString("project_id"); //example = 54436172f800926beb1cb0a2
                        String status = data.getString("status"); //example = playing
                        String last_action = data.getString("last_action"); //example = 1463213653
                        String node = data.getString("node"); //example = 9001
                        String checksum = data.getString("checksum"); //example = 05c41af16e7c3c58dc90b03ddd4a3e13
                        //String worldcup_id = data.getString("worldcup_id");
                        String level = data.getString("level");
                        String _project = data.getString("project");

                        JSONObject project = new JSONObject(_project);
                        String _id_ = data.getString("_id");
                        String _status = data.getString("status"); //example = active
                        String _AI = data.getString("AI"); //example = no
                        String _game = data.getString("game"); //example = Othello
                        String _player_number = data.getString("player_number"); //example = 2
                        String _timeout = data.getString("time_out"); //example = 90

                        String actions = data.getString("actions");
                        JSONArray actions_array = new JSONArray(actions);
                        String changes = data.getString("changes");
                        JSONArray changes_array = new JSONArray(changes);



                        String users = data.getString("users");
                        JSONArray users_array = new JSONArray(users);

                        JSONObject user0 = users_array.getJSONObject(0);
                        JSONObject user1 = users_array.getJSONObject(1);

                        String user_id_0 = user0.getString("user_id");
                        String handout_0 = user0.getString("handout"); //example = true
                        String winner_0 = user0.getString("winner"); //example = false
                        String last_action_0 = user0.getString("last_action"); //example = 1463206240
                        String ip_0 = user0.getString("ip"); //example = 100.100.100.100
                        String user_level_0 = user0.getString("user_level");
                        String join_0 = user0.getString("join"); //example = false
                        String nickname_0 = user0.getString("nickname"); //example = ali
                        String options_0 = user0.getString("options");
                        String accessbility_0 = user0.getString("accessbility");






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
