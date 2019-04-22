package pc.jaakon.stockmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ListView resultView;
    private RequestQueue requestQueue;
    private String[] abbreviations = { "AAPL", "GOOGL", "FB", "NOK" };
    public String companyName;
    public ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> stocks;
    public JSONObject stockObject;
    private String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = findViewById(R.id.stock_list);
        requestQueue = Volley.newRequestQueue(this);
        stocks = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stocks);

        parseJson();
    }



    public void parseJson() {
        for (int i = 0; i < abbreviations.length; i++) {
            final String singleAbbr = abbreviations[i];


            String URL = "https://financialmodelingprep.com/api/company/price/" + singleAbbr + "?datatype=json";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            switch (singleAbbr){
                                case "NOK":
                                    companyName = "Nokia";
                                    break;
                                case "AAPL":
                                    companyName = "Apple";
                                    break;
                                case "FB":
                                    companyName = "Facebook";
                                    break;
                                case "GOOGL":
                                    companyName = "Google";
                                    break;
                            }
                            try {
                                stockObject = response.getJSONObject(singleAbbr);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            try {
                                value = String.valueOf(stockObject.get("price"));
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            stocks.add(companyName + ": " + value + " USD");
                            inflateView();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(request);

        }
    }

    public void inflateView() {
        resultView.setAdapter(arrayAdapter);
    }
}
