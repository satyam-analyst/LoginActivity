package in.ac.sharda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    EditText EditTextUsername,EditTextPassword;
    Button btnLogin;
    ListView list;
    AsyncHttpClient client;
    RequestParams params;

    ArrayList listdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditTextUsername=(EditText)findViewById(R.id.EditTextUsername);
        EditTextPassword=(EditText)findViewById(R.id.EditTextPassword);
        listdata = new ArrayList<>();
        list=(ListView)findViewById(R.id.list);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(EditTextUsername.getText().toString()))
                {
                    EditTextUsername.setError("Enter Name");
                }
                else if (TextUtils.isEmpty(EditTextPassword.getText().toString()))
                {
                    EditTextPassword.setError("Enter Password");
                }
                else{
                    client=new AsyncHttpClient();
                    params=new RequestParams();

                    params.put("title",EditTextUsername.getText().toString());
                    params.put("body",EditTextPassword.getText().toString());

                    /*
                    client.post("https://jsonplaceholder.typicode.com/posts", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    */


                    client.get("https://jsonplaceholder.typicode.com/posts", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String data= new String(responseBody);
                            Log.d("satyam",data);

                            try {
                                JSONArray array= new JSONArray(data);
                                for (int i=0; i<array.length();i++){
                                    JSONObject jsonobj = array.getJSONObject(i);
                                    int userId= jsonobj.getInt("userId");
                                    int id = jsonobj.getInt("Id");
                                    String title= jsonobj.getString("title");
                                    String body= jsonobj.getString("body");
                                    listdata.add(userId+"\n"+id+"\n"+title+"\n"+body);
                                    ArrayAdapter adapter= new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1);
                                    list.setAdapter(adapter);

                                }
                            }
                            catch (JSONException je){
                                je.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }
}
