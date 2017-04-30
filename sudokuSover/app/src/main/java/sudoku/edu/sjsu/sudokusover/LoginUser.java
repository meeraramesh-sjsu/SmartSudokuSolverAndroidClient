package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        email = (TextView) findViewById(R.id.emailId);
        token = (TextView) findViewById(R.id.token);
        validationResult = (EditText) findViewById(R.id.validationResult);

    }

    public  void  validateAndPlay(View V) throws JSONException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://192.168.1.6:8080/sudoku/validate";

                JSONObject request = new JSONObject();
                try {

                    request.put("emailId", email.getText().toString());
                    request.put("token", token.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

                String validate = restTemplate.postForObject(url, entity, String.class);
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
}