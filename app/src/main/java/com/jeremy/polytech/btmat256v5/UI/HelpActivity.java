package com.jeremy.polytech.btmat256v5.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.jeremy.polytech.btmat256v5.R;

public class HelpActivity extends AppCompatActivity {

    WebView aboutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Aide");

        aboutView=(WebView)findViewById(R.id.aboutWebView);
        aboutView.loadUrl("file:///android_asset/about.html");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_close_help:
                finish();
                break;
            case R.id.menu_item_protocol_help:
                aboutView.loadUrl("file:///android_asset/protocol.html");
                break;
            case R.id.menu_item_help_help:
                aboutView.loadUrl("file:///android_asset/about.html");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
