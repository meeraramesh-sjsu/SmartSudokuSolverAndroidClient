package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class LoginUser extends Activity {
    private TextView email;
    private TextView token;
    private TextView validationResult;
    private ListView listview;
    private String emailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        email = (TextView) findViewById(R.id.emailId);
        validationResult = (EditText) findViewById(R.id.validationResult);
        listview = (ListView)findViewById(R.id.listView);
        listview.setOnItemClickListener(new ListClickHandler());
    }

    public void getTokens(View v) throws JSONException {
        emailId = email.getText().toString();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://192.168.1.6:8080/sudoku/gettokens";

                JSONObject request = new JSONObject();
                try {

                    request.put("emailid", email.getText().toString());
                   } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

                final String tokens = restTemplate.postForObject(url, entity, String.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(tokens.contains("No"))
                            Toast.makeText(getApplicationContext(),tokens, Toast.LENGTH_LONG).show();
                        else {
                            String[] tokenStr = tokens.split("\\r?\\n");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_row, R.id.listText, tokenStr);
                            listview.setAdapter(adapter);
                        }
                    }
                });
            }
            });
        thread.start();
            }

    public  void  validateAndPlay(final String token) throws JSONException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://192.168.1.6:8080/sudoku/validate";

                JSONObject request = new JSONObject();
                try {

                    request.put("emailId", emailId);
                    request.put("token", token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

                final String validate = restTemplate.postForObject(url, entity, String.class);
                if (validate.contains("Invalid")) {
                    validationResult.setText(validate);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }});

            thread.start();
}

    private class ListClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.listText);
            String text = listText.getText().toString();
            try {
                validateAndPlay(text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onHome(View v) {
        startActivity(new Intent(this, StartActivity.class));
    }
}