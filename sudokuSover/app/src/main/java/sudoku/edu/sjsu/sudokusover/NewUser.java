package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Karthik on 4/21/2017.
 */

public class NewUser extends Activity {
    public static String EXTRA_MESSAGE;
    private EditText name;
    private EditText emailid;
    private EditText objid;
    private EditText paymplan;
    private EditText amount;
    private String BASE_URL = "http://192.168.1.6:8080/sudoku";
    //http://192.168.1.5:8080/sudoku/newuser/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        name = (EditText) findViewById(R.id.name);
        emailid = (EditText) findViewById(R.id.emailId);
        objid = (EditText) findViewById(R.id.objectInstanceId);
        paymplan = (EditText) findViewById(R.id.paymentPlan);
        amount = (EditText) findViewById(R.id.amount);
    }


    public void registerNewUser(View v) throws JSONException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    RestTemplate restTemplate = new RestTemplate();
                   String newUserRestUrl = BASE_URL + "/newuser/";

                    // create request body
                    JSONObject request = new JSONObject();
                    request.put("objectInstanceId", objid.getText().toString());
                    request.put("name", name.getText().toString());
                    request.put("paymentPlan", paymplan.getText().toString());
                    request.put("emailId",emailid.getText().toString());
                    request.put("amount",amount.getText().toString());

                    // set headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

                        String tokens =  restTemplate.postForObject(newUserRestUrl,entity,String.class);

                    //Toast.makeText(getApplicationContext(),
                      //      getString(R.string.new_user),
                       //     Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Tokens.class);

                    intent.putExtra(EXTRA_MESSAGE, tokens);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
