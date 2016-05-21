package com.pegtura.usnfinder;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    File path;
    File file;
    TextView textView;
    Button button;
    EditText editText;
    String contents, contents1;
    String[] brokenString;
    Map<String, String> myMap = new HashMap<>();
    Map<String, String> myMap1 = new HashMap<>();
    String text;
    ArrayList<String> myList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        //THIS IS TO CONNECT TO MYSQL DATABASE AND GET RESPONSE
        DownloadData downloadData = new DownloadData();
        try {
            contents = downloadData.execute("http://pegtura.com/getdata.php").get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //THIS IS TO COMPILE DOWNLOADED DATA AND STORE INTO A HASHMAP
        Pattern pattern = Pattern.compile("<td>(.*?)</td><td>(.*?)</td>");
        Matcher matcher = pattern.matcher(contents);
        while(matcher.find()) {
            myMap.put(matcher.group(1), matcher.group(2));
        }

        //THIS IS TO FIND ROOM NUMBER OF ALLOTTED USN ON BUTTON CLICK
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    text = editText.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textView.setText(myMap.get(text));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("Toolbar Pressed", "LOAD");
            try {
                path = Environment.getExternalStorageDirectory();
                file = new File(path + "/Download", "Hi.txt");
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                contents1 = new String(bytes);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //THIS IS TO COMPILE DATA FROM TEXT FILE ON DEVICE
            if(!contents1.isEmpty()) {
                brokenString = contents1.split("\n");
                //LOGGING IS TO CHECK BEHAVIOUR OF OUTPUT
                //Log.i("getbroken", brokenString[0] + brokenString[1] + brokenString[2]);
                for (String string : brokenString) {
                    Pattern pattern1 = Pattern.compile("(.*?) (.*)");
                    Matcher matcher1 = pattern1.matcher(string);
                    while (matcher1.find()) {
                        myMap1.put(matcher1.group(1), matcher1.group(2));
                        //LOGGING IS TO CHECK BEHAVIOUR OF OUTPUT
                        Log.i("getbroken1", matcher1.group(1) + " " + matcher1.group(2));
                    }
                }
            }

            //THIS IS TO POST HTTP REQUESTS
            if(!myMap1.isEmpty()) {
                PostData postData = new PostData();
                try {
                    for (Map.Entry<String, String> entry : myMap1.entrySet()) {
                        myList.add("http://pegtura.com/signup.php?usn="
                                + entry.getKey() + "&roomno=" + entry.getValue());
                    }
                    postData.execute(myList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
