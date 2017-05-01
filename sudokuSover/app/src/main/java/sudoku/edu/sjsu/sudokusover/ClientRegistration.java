package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientRegistration extends Activity {

    private RestTemplate restTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);
    }

    private EditText objectId;
    private EditText endpoint;
    private EditText lifetim;
    private EditText notificationStoring;
    private EditText binding;
    private EditText regUpdate;
    private Button save;
    private Button update;
    private Button delete;
    private String endPointName = "android";
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-04-30 15:34:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        objectId = (EditText)findViewById( R.id.objectId );
        endpoint = (EditText)findViewById( R.id.endpoint );
        lifetim = (EditText)findViewById( R.id.lifetim );
        notificationStoring = (EditText)findViewById( R.id.notificationStoring );
        binding = (EditText)findViewById( R.id.binding );
        regUpdate = (EditText)findViewById( R.id.regUpdate );
        save = (Button)findViewById( R.id.save );
        update = (Button)findViewById( R.id.update );
        delete = (Button)findViewById( R.id.delete );

        save.setOnClickListener((View.OnClickListener) this);
        update.setOnClickListener((View.OnClickListener) this);
        delete.setOnClickListener((View.OnClickListener) this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-04-30 15:34:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */

    public void onCreate(View v) {
        createThread("create");
    }

    public void onUpdate(View v) {
        createThread("update");
    }

    public void onDelete(View v) {
        createThread("delete");
    }

    public String getRegisterURI() throws JSONException {

        MongoClient mongoClient = new MongoClient( "192.168.1.6" , 27018);
        DB db = mongoClient.getDB( "rasberryPiDB" );
        DBCollection collection2 = db.getCollection("LWM2MSecurityObject");

        BasicDBObject whereQuery = new BasicDBObject();
        //Converting XML Edit Text to Java Edit Text
        whereQuery.put("_id", endpoint.getText().toString());							// check the specific id
        DBCursor cursor = collection2.find(whereQuery);
        if(cursor.hasNext()) {
            JSONObject jsonObject = new JSONObject(cursor.next().toString());
            return jsonObject.getString("LWM2M Server URI");
        }
        return "Invalid endpoint name";
    }

    public void makeToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
        });
}

    public void createRegister() throws JSONException {
        final String  url = getRegisterURI();
        if(url.contains("Invalid")) {
            makeToast(url);
            return;
        }
        restTemplate = new RestTemplate();
        JSONObject request = new JSONObject();
        request.put("_id", objectId.getText().toString());
        request.put("LifeTime", lifetim.getText().toString());
        request.put("Notification Storing",notificationStoring.getText().toString());
        request.put("Binding",binding.getText().toString());
        request.put("Registration Update Trigger",regUpdate.getText().toString());

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<String>(request.toString(), headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        makeToast(result);
    }

    public void updateRegister() throws JSONException {
        final String  url = getRegisterURI();
        if(url.contains("Invalid")) {
            makeToast(url);
            return;
        }
        restTemplate = new RestTemplate();
        JSONObject request = new JSONObject();
        request.put("_id", objectId.getText().toString());
        request.put("LifeTime", lifetim.getText().toString());
        request.put("Binding",binding.getText().toString());

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<String>(request.toString(), headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT,entity,String.class,Object.class);
        makeToast(result.getBody());
    }

    public void deleteRegister() {

    }

    public void createThread(final String function) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        if(function.equals("create"))
                        createRegister();
                        else if(function.equals("update"))
                        updateRegister();
                        else if(function.equals("delete"))
                            deleteRegister();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        });
        thread.start();
    }
}
