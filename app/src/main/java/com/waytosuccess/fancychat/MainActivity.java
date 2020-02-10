package com.waytosuccess.fancychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView messagingListView;
    private FancyMessageAdapter fancyMessageAdapter;
    private Button btnSendMsg;
    private ProgressBar progressBar;
    private EditText editMsg;
    private ImageButton btnSendImg;

    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = "Default User";

        List<FancyMessage> listMessages = new ArrayList<>();
        fancyMessageAdapter = new FancyMessageAdapter(MainActivity.this,
                R.layout.message_item, listMessages);
        try {
            messagingListView.setAdapter(fancyMessageAdapter);
        } catch (NullPointerException nlpe){
           Log.d("AdapterMyErr", "Adapter: " + fancyMessageAdapter.toString());
        }
        btnSendMsg = findViewById(R.id.btnSendMsg);
        btnSendImg = findViewById(R.id.btnSendImg);
        progressBar = findViewById(R.id.progressBar);
        editMsg = findViewById(R.id.editMsg);
        messagingListView = findViewById(R.id.messagingListView);

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0){
                    btnSendMsg.setEnabled(true);
                } else {
                    btnSendMsg.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editMsg.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(500)});
    }

    public void btnSendMsg(View view) {
        editMsg.setText("");
    }
}
