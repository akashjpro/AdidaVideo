package com.adida.akashjpro.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adida.akashjpro.activity.LoginActivity;
import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.activity.ProfileActivity;
import com.adida.akashjpro.fragment.FragBottom_Chat;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Chat;
import com.adida.akashjpro.model.Mau;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.adida.akashjpro.activity.MainActivity.ArrayColor;
import static com.adida.akashjpro.activity.MainActivity.mUser;
import static com.adida.akashjpro.adapter.CommentRecyclerView.mContext;

/**
 * Created by Aka on 12/31/2016.
 */

public class ChatAdapter extends ArrayAdapter<Chat> {
    Activity context;
    int resource;
    List<Chat> listChat;


    public ChatAdapter(Activity context, int resource, List<Chat> listChat) {
        super(context, resource, listChat);
        this.context = context;
        this.resource = resource;
        this.listChat = listChat;
    }

    class ViewHoler {
        TextView name, messages, idUser;
        LinearLayout containLayout;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = convertView;
        ViewHoler holer = new ViewHoler();
        if(view == null){
            view = inflater.inflate(this.resource, null);
            holer.name          = (TextView) view.findViewById(R.id.txtName);
            holer.messages      = (TextView) view.findViewById(R.id.txtMessage);
            holer.idUser        = (TextView) view.findViewById(R.id.idUser);
            holer.containLayout = (LinearLayout) view.findViewById(R.id.containItem);
            view.setTag(holer);
        }
        else {
            holer = (ViewHoler) view.getTag();
        }
        //return current chat

        final ViewHoler finalHoler = holer;

        holer.containLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser != null) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("idUser", finalHoler.idUser.getText().toString());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.scale_zoom_center, R.anim.normal);
                }else {
                    final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                    alertLogin.setIcon(R.mipmap.ic_launcher);
                    alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                    alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                    alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                    alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alertLogin.show();

                }
            }
        });

        Chat chat = this.listChat.get(position);
        if(chat.getIdUser() != null) {
            MainActivity.mDatabase.child("User").child(chat.getIdUser()).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    finalHoler.name.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        holer.messages.setText(chat.getMessages());
        holer.idUser.setText(chat.getIdUser());
        if(FragBottom_Chat.mColorMap.get(chat.getIdUser()) != null){
            int blue  = FragBottom_Chat.mColorMap.get(chat.getIdUser()).blue;
            int green = FragBottom_Chat.mColorMap.get(chat.getIdUser()).green;
            int red   = FragBottom_Chat.mColorMap.get(chat.getIdUser()).red;
            holer.name.setTextColor(Color.rgb(red, green, blue));
        }
        else {
            /**
             * Create Radom Color  and set color for user
             */
            Random random = new Random();
            int color = random.nextInt(256);
            ArrayColor.set(0, 255);
            ArrayColor.set(1, 0);
            ArrayColor.set(2, color);
            Collections.shuffle(ArrayColor);
            int R = ArrayColor.get(0);
            int G = ArrayColor.get(1);
            int B = ArrayColor.get(2);
            holer.name.setTextColor(Color.rgb(R, G, B));
            FragBottom_Chat.mColorMap.put(chat.getIdUser(), new Mau(R, G, B));
        }

        return view;
    }
}



