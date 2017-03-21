package com.example.myhookview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myhookview.util.UtilHolder;

public class MainActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"button被点击了",Toast.LENGTH_LONG).show();
            }
        });
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                UtilHolder.hookView(button);
            }
        });
    }
}
