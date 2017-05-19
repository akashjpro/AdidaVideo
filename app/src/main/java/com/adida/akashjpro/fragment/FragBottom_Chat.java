package com.adida.akashjpro.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.adida.akashjpro.activity.LoginActivity;
import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.adapter.ChatAdapter;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Chat;
import com.adida.akashjpro.model.Mau;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aka on 12/30/2016.
 */

public class FragBottom_Chat extends Fragment{
    public static ListView mListView;
    public static ChatAdapter chatAdapter;
    public static ArrayList<Chat> listChat;
    public static ChildEventListener mChildEventListener;


    private EditText mMessagesEdittext;
    private Button   mSentButton;

    private View mView;
    public static String idVideo = "";
    public static DatabaseReference mDatabase;
    private String mMessage;
    public  static Map<String, Mau> mColorMap;
    private Activity mContext;
    AlertDialog.Builder alertLogin;
    boolean isShow = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_bottom_chat, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addControls();
        mColorMap = new HashMap<>();
        mContext = getActivity();
        eventListeners();
        return mView;
    }


    public void addControls() {
        mMessage = "";
        listChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), R.layout.item_chat, listChat);
        mListView = (ListView) mView.findViewById(R.id.listChat);
        mListView.setAdapter(chatAdapter);

        mMessagesEdittext = (EditText) mView.findViewById(R.id.edtMessages);
        mSentButton = (Button) mView.findViewById(R.id.btnSent);

    }


    static public void getId(final String id){
        listChat.clear();
        idVideo = id;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                listChat.add(chat);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("Chat").child("chat").child(idVideo).addChildEventListener(mChildEventListener);


    }

    public void eventListeners() {
        mSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mUser != null) {
                    mMessage = mMessagesEdittext.getText().toString().trim();
                    if (mMessage.equals("")) {
                        Toast.makeText(getActivity(), "Xin nhập nội dung chat!!!", Toast.LENGTH_SHORT).show();

                    } else {
                        String id = MainActivity.mUser.getUid();
                        String name = MainActivity.name;
                        Chat chat = new Chat(id, name, mMessage);
                        mDatabase.child("Chat").child("chat").child(idVideo).push().setValue(chat);

                        mMessagesEdittext.setText("");// set "" for content messages when sented messages
                    }
                }else {
                    alertLogin = new AlertDialog.Builder(getActivity());
                    alertLogin.setIcon(R.mipmap.ic_launcher);
                    alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                    alertLogin.setMessage("Bạn có muốn đăng nhập để chat không?");
                    alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alertLogin.show();

                    mMessagesEdittext.setText("");
                    isShow = false;
                }

            }
        });

        mMessagesEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(MainActivity.mUser != null){
                        mSentButton.setEnabled(true);
                    }
                    else {
                        if (!isShow) {
                            alertLogin = new AlertDialog.Builder(getActivity());
                            alertLogin.setIcon(R.mipmap.ic_launcher);
                            alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                            alertLogin.setMessage("Bạn có muốn đăng nhập để chat không?");
                            alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertLogin.show();
                            isShow = true;

                            return;
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
