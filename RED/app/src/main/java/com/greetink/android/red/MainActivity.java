package com.greetink.android.red;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void classic(View view){
        startActivity(new Intent(this, Classic.class));
    }
    public void modern(View view){
        startActivity(new Intent(this, Modern.class));
    }

    public void info(View view){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.info_creator);
        if(relativeLayout.getVisibility() == View.GONE){
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.GONE);
        }
    }
}
