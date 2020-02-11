package com.waytosuccess.fancychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GET_IMAGE_LOCAL_STORAGE = 113;
    private ListView messagingListView;
    private FancyMessageAdapter fancyMessageAdapter;
    private Button btnSendMsg;
    private ProgressBar progressBar;
    private EditText editMsg;
    private ImageButton btnSendImg;

    private String userName;

    private FirebaseDatabase database;
    private DatabaseReference messagesDatabaseReference;
    private ChildEventListener messagesChildEventListener;

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener usersChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        messagesDatabaseReference = database.getReference().child("messages");
        usersDatabaseReference = database.getReference().child("users");

        Intent intent  = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("user_name");
        } else {
            userName = "Default User";
        }
        List<FancyMessage> listMessages = new ArrayList<>();
        fancyMessageAdapter = new FancyMessageAdapter(this,
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

        messagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FancyMessage message = dataSnapshot.getValue(FancyMessage.class);
                fancyMessageAdapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        messagesDatabaseReference.addChildEventListener(messagesChildEventListener);

        usersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    userName = user.getName();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addChildEventListener(usersChildEventListener);
    }

    public void btnSendMsg(View view) {

        FancyMessage fancyMessage = new FancyMessage();
        fancyMessage.setText(editMsg.getText().toString());
        fancyMessage.setName(userName);
        fancyMessage.setImageURL(null);

        messagesDatabaseReference.push().setValue(fancyMessage);

        editMsg.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendImg(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Choose an image"), GET_IMAGE_LOCAL_STORAGE);
    }
}
