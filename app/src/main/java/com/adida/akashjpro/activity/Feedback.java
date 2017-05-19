package com.adida.akashjpro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adida.akashjpro.livevideo.R;

import static com.adida.akashjpro.adapter.CommentRecyclerView.mContext;

public class Feedback extends AppCompatActivity {

    Button btnGui;
    EditText content;
    boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        btnGui = (Button) findViewById(R.id.btnGui);
        content = (EditText) findViewById(R.id.editTextNĐongGop);
        isShow = false;

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mUser != null) {
                    String nd = content.getText().toString().trim();
                    if (!nd.isEmpty()) {

                        MainActivity.mDatabase.child("Feadback").child(MainActivity.mUser.getUid()).setValue(content.getText().toString().trim());
                        Toast.makeText(Feedback.this, "Đã gửi ý kiến đóng góp", Toast.LENGTH_SHORT ).show();
                        content.setText("");

                    }else {
                        Toast.makeText(Feedback.this, "Xin nhập nội dung đóng góp!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(!isShow) {
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
                        isShow = true;
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
