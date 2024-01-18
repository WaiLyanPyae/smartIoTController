package com.example.smartiotcontroller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class NextActivity extends AppCompatActivity {
    private static final String TAG = NextActivity.class.getSimpleName();
    private Button btnOn, btnOff, signOut;
    private TextView tvLED_STATUS, tvSelectedColorCode;
    private ColorPickerView colorPickerView;
    private LinearLayout llSelectedColor;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private int RED;
    private int GREEN;
    private int BLUE;
    private String HEX;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        database = FirebaseDatabase.getInstance("https://new-fyp-c8ff5-default-rtdb.asia-southeast1.firebasedatabase.app/");
        tvLED_STATUS = (TextView) findViewById(R.id.tvLED_STATUS);
        btnOn = (Button) findViewById(R.id.btnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance("https://new-fyp-c8ff5-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("LED_STATUS");
                myRef.setValue(1);
                myRef = database.getReference("RED");
                myRef.setValue(RED);
                myRef = database.getReference("GREEN");
                myRef.setValue(GREEN);
                myRef = database.getReference("BLUE");
                myRef.setValue(BLUE);
                Log.e("LED_STATUS", "1");
                Log.e(TAG + " RGB ", "(" + RED + GREEN + BLUE + ")");
                tvLED_STATUS.setText("LED IS ON");
                tvLED_STATUS.setTextColor(Color.GREEN);
            }
        });
        btnOff = (Button) findViewById(R.id.btnOff);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance("https://new-fyp-c8ff5-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("LED_STATUS");
                myRef.setValue(0);
                Log.e("LED_STATUS", "0");
                tvLED_STATUS.setText("LED IS OFF");
                tvLED_STATUS.setTextColor(Color.RED);
            }
        });
        llSelectedColor = (LinearLayout) findViewById(R.id.llSelectedColor);
        colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        colorPickerView.setActionMode(ActionMode.ALWAYS);
        tvSelectedColorCode = (TextView) findViewById(R.id.tvSelectedColorCode);
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                llSelectedColor.setBackgroundColor(envelope.getColor());
                RED = Color.red(envelope.getColor());
                GREEN = Color.green(envelope.getColor());
                BLUE = Color.blue(envelope.getColor());
                HEX = envelope.getHexCode();
                tvSelectedColorCode.setText(" HexCode: " + envelope.getHexCode() + " \n RGB: " + "(" + RED + "," + GREEN + "," + BLUE + ")");
                myRef = FirebaseDatabase.getInstance("https://new-fyp-c8ff5-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("LED_STATUS");
                myRef.setValue(1);
                myRef = database.getReference("RED");
                myRef.setValue(RED);
                myRef = database.getReference("GREEN");
                myRef.setValue(GREEN);
                myRef = database.getReference("BLUE");
                myRef.setValue(BLUE);
                myRef = database.getReference("HEX");
                myRef.setValue(HEX);
                Log.e("LED_STATUS", "1");
                Log.e(TAG + " RGB ", "(" + RED + GREEN + BLUE + ")");
                tvLED_STATUS.setText("LED IS ON");
                tvLED_STATUS.setTextColor(Color.GREEN);
            }
        });
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(NextActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    public void signOut() {
        auth.signOut();
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
