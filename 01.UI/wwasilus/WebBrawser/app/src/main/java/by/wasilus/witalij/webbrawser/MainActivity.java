package by.wasilus.witalij.webbrawser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Witold on 07.02.2015.
 */
public class MainActivity extends Activity {

    Button btForward;
    Button btBack;
    WebView wvMain;
    EditText etUrl;
    ArrayList<String> urlList;
    int position = -1;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urlList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btForward = (Button) findViewById(R.id.bt_forward);
        btBack = (Button) findViewById(R.id.bt_back);
        wvMain = (WebView) findViewById(R.id.wv_main);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.getSettings().setBuiltInZoomControls(true);
        wvMain.getSettings().setAppCacheEnabled(true);
        wvMain.setWebViewClient(new MyWebView());
        etUrl = (EditText) findViewById(R.id.et_url);
        checkingPosition();

        etUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    try {
                        new URL(etUrl.getText().toString());
                        position++;
                        urlList.add(position, etUrl.getText().toString());
                        wvMain.loadUrl(urlList.get(position));
                        checkingPosition();
                        adapter.notifyDataSetChanged();
                    } catch (MalformedURLException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.url_error),
                                Toast.LENGTH_LONG).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    wvMain.goBack();
                    checkingPosition();
                }
            }
        });

        btForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < urlList.size() - 1) {
                    position++;
                    wvMain.loadUrl(urlList.get(position));
                    checkingPosition();
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            wvMain.restoreState(savedInstanceState);
            urlList = savedInstanceState.getStringArrayList("urlL");
            position = savedInstanceState.getInt("posit");
            checkingPosition();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wvMain.saveState(outState);
        outState.putStringArrayList("urlL", urlList);
        outState.putInt("posit", position);
    }

    class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            position++;
            urlList.add(position, url);
            wvMain.loadUrl(url);
            checkingPosition();
            return true;
        }
    }

    private void checkingPosition() {
        if (position < urlList.size() - 1) {
            btForward.setEnabled(true);
        } else {
            btForward.setEnabled(false);
        }
        if (position > 1) {
            btBack.setEnabled(true);
        } else {
            btBack.setEnabled(false);
        }
    }
}


