package mx.chavezp.openweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "openweather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView iv_weather = findViewById(R.id.iv_weather);
        final TextView tv_temp = findViewById(R.id.tv_temp);
        final TextView tv_city = findViewById(R.id.tv_city);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.68/clima";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.has("name")){
                                String city = response.getString("name");
                                tv_city.setText("" + city);
                            }
                            if (response.has("weather")) {
                                JSONArray weatherArray = response.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);

                                if (weather.has("icon")) {
                                    String icon = weather.getString("icon");
                                    int identificador = getResources().getIdentifier("imagen_" + icon, "drawable", getPackageName());
                                    iv_weather.setImageDrawable(getResources().getDrawable(identificador, null));
                                }
                            }

                            if (response.has("main")) {
                                JSONObject main = response.getJSONObject("main");
                                Double t = main.getDouble("temp");
                                int temp = t.intValue();

                                tv_temp.setText("" + temp + " \u00b0" + "C");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,error.getMessage());
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
