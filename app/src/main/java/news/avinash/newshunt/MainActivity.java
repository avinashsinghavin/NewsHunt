package news.avinash.newshunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageurl;
    private TextView title, description, bottonview, content;
    boolean doubleBackToExitPressedOnce = false;
    private int k = 0;
    private int MIN_DISTANCE = 100;
    private float x1, x2, downX,upX;
    private int length = 0, val = 1;
    private ProgressDialog pd;
    private ScrollView scrollView;
    // ---------------------for api===========
    private String category;
    private String business, entertainment, general, health, science, sports, technology;
    //====================
    private JSONObject jsonObj;
    private JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageurl = (ImageView) findViewById(R.id.imageurl);
        description = (TextView) findViewById(R.id.description);
        title = (TextView) findViewById(R.id.title);
        bottonview = (TextView) findViewById(R.id.bottomview);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        content = (TextView) findViewById(R.id.content);
        String url = ("https://newsapi.org/v2/top-headlines?country=in&category=general&apiKey=fdf14b15b1444bf18343d18b109b48e6");
        new JsonTask().execute(url);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                val--;
                                if (val > 0 && val < length) {
                                    try {
                                        jsonArray = jsonObj.getJSONArray("articles");
                                        length = jsonArray.length();
                                        JSONObject rec = jsonArray.getJSONObject(val);
                                        title.setText(rec.getString("title"));
                                        description.setText("Description : " + rec.getString("description"));
                                        content.setText(rec.getString("content") +  "\n");
                                        bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                        Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "left to right if" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    val = length - 1;
                                    try {
                                        jsonArray = jsonObj.getJSONArray("articles");
                                        length = jsonArray.length();
                                        JSONObject rec = jsonArray.getJSONObject(val);
                                        title.setText(rec.getString("title"));
                                        description.setText("Description : " + rec.getString("description"));
                                        content.setText(rec.getString("content") +  "\n");
                                        bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                        Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "left to right else" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            // Right to left swipe action
                            else {
                                val++;
                                if (val > 0 && val < length) {
                                    try {
                                        jsonArray = jsonObj.getJSONArray("articles");
                                        length = jsonArray.length();
                                        JSONObject rec = jsonArray.getJSONObject(val);
                                        title.setText(rec.getString("title"));
                                        description.setText("Description : " + rec.getString("description"));
                                        content.setText(rec.getString("content") +  "\n");
                                        bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                        Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "right to left if" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    val = 1;
                                    try {
                                        jsonArray = jsonObj.getJSONArray("articles");
                                        length = jsonArray.length();
                                        JSONObject rec = jsonArray.getJSONObject(val);
                                        title.setText(rec.getString("title"));
                                        description.setText("Description : " + rec.getString("description"));
                                        Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                                        content.setText(rec.getString("content") +  "\n");
                                        bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "Right to left else" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        } else {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return false;
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    k = 1;
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                jsonObj = new JSONObject(result);
                String status = jsonObj.getString("status");
                if (status.equals("ok")) {
                    jsonArray = jsonObj.getJSONArray("articles");
                    length = jsonArray.length();
                    JSONObject rec = jsonArray.getJSONObject(val);
                    title.setText(rec.getString("title"));
                    description.setText("Description : " + rec.getString("description") + " \n ");
                    content.setText(rec.getString("content") +  "\n");
                    bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                    //==================Image Url  urlToImage
                    Picasso.get().load(rec.getString("urlToImage")).into(imageurl);
                    //Picasso.with(context).load(rec.getString("urlToImage")).into(imageurl);
                    //===============================
                } else
                    Toast.makeText(MainActivity.this, " Server Error", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, " Connection Problem " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //================================Api Request and display ===================

    //==============================================================================



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        val--;
                        if (val > 0 && val < length) {
                            try {
                                jsonArray = jsonObj.getJSONArray("articles");
                                length = jsonArray.length();
                                JSONObject rec = jsonArray.getJSONObject(val);
                                title.setText(rec.getString("title"));
                                description.setText("Description : " + rec.getString("description"));
                                content.setText(rec.getString("content") +  "\n");
                                bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                            } catch (Exception e) {
                                Toast.makeText(this, "left to right if" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            val = length - 1;
                            try {
                                jsonArray = jsonObj.getJSONArray("articles");
                                length = jsonArray.length();
                                JSONObject rec = jsonArray.getJSONObject(val);
                                title.setText(rec.getString("title"));
                                description.setText("Description : " + rec.getString("description"));
                                content.setText(rec.getString("content") +  "\n");
                                bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                            } catch (Exception e) {
                                Toast.makeText(this, "left to right else" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    // Right to left swipe action
                    else {
                        val++;
                        if (val > 0 && val < length) {
                            try {
                                jsonArray = jsonObj.getJSONArray("articles");
                                length = jsonArray.length();
                                JSONObject rec = jsonArray.getJSONObject(val);
                                title.setText(rec.getString("title"));
                                description.setText("Description : " + rec.getString("description"));
                                content.setText(rec.getString("content") +  "\n");
                                bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                                Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                            } catch (Exception e) {
                                Toast.makeText(this, "right to left if" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            val = 1;
                            try {
                                jsonArray = jsonObj.getJSONArray("articles");
                                length = jsonArray.length();
                                JSONObject rec = jsonArray.getJSONObject(val);
                                title.setText(rec.getString("title"));
                                description.setText("Description : " + rec.getString("description"));
                                Picasso.get().load(rec.getString("urlToImage")).into(imageurl);// install Dependenency of picasso implemention
                                content.setText(rec.getString("content") +  "\n");
                                bottonview.setText(rec.getString("publishedAt") + "    " + rec.getString("author"));
                            } catch (Exception e) {
                                Toast.makeText(this, "Right to left else" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /*public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            Toast.makeText(this, "display", Toast.LENGTH_SHORT).show();
            // ====================display Image in pop up screen ==========
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.display_image_start);
//            dialog.setTitle("Dialog box");
//            Button button = (Button) dialog.findViewById(R.id.dismiss);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
            Dialog builder = new Dialog(this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });

            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.aaaa);
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            builder.show();
            //========================end===================================
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

    }
    */
}
