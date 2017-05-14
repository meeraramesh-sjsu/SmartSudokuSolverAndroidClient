package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class BootStrap extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_strap);
        findViews();
    }

    private BasicInfo basicInfo;
    private MongoClient mongoClient = new MongoClient(basicInfo.mongoIp,basicInfo.mongoPort);


    private String IpAddress;
    private EditText endpoint;
    private Button bootStrap;
    private EditText serverOutput;
    private RestTemplate restTemplate;
    private JSONObject jsonObject;
    private HttpHeaders httpHeaders;
    private HttpEntity httpEntity;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-13 10:03:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        endpoint = (EditText)findViewById( R.id.bootStrap_endpoint );
        bootStrap = (Button)findViewById( R.id.bootStrap_bootStrap );
        serverOutput = (EditText)findViewById( R.id.serverOutput );
    }

    public void setServerOut(final String serverInfo) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                serverOutput.setText(serverInfo);
            }
        });
    }

    public void onHome(View v) {
        startActivity(new Intent(this, StartActivity.class));
    }

    public void onBootStrap(View v) {
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            String url = basicInfo.ipAddress + "/bootStrap";
            restTemplate = new RestTemplate();
            jsonObject = new JSONObject();
            try{
                jsonObject.put("endpoint",endpoint.getText().toString());
                httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpEntity = new HttpEntity<String>(jsonObject.toString(),httpHeaders);
                String serverInfo = restTemplate.postForObject(url, httpEntity, String.class);
                setServerOut(serverInfo);

                String object[] = serverInfo.split("\\r?\\n");
                JSONObject LWM2MSecurity = new JSONObject(object[0]);
                JSONObject LWM2MServer = new JSONObject(object[1]);

                DB db = mongoClient.getDB("rasberryPiDB");
                BasicDBObject Securitydoc = new BasicDBObject().parse(LWM2MSecurity.toString());
                BasicDBObject Serverdoc = new BasicDBObject().parse(LWM2MServer.toString());

                DBCollection collection = db.getCollection("LWM2MSecurityObject");
                collection.insert(Securitydoc);

                DBCollection collection2 = db.getCollection("LWM2MServerObject");
                collection2.insert(Serverdoc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
        t.start();
    }
}