package com.koreait.gumzimaregist.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.koreait.gumzimaregist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegistActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    private Boolean check = false;  // 아이디 중복체크
    private AlertDialog dialog;

    private Button btn_check, btn_search, registerbutton; // 중복체크, 주소검색, 회원가입 (버튼)
    // 회원가입 시 입력하는 공간..
    EditText idText, passText, nameText, emailText, phoneText, addr1Text, addr2Text;

    Handler handler;
    RegistActivity registActivity;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registActivity = this;
        setContentView(R.layout.activity_regist);

        //back 버튼 추가
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("회원가입");

        // 중복체크, 주소검색, 회원가입 (버튼)
        btn_check = (Button) this.findViewById(R.id.btn_check);
        btn_search = (Button) this.findViewById(R.id.btn_search);
        registerbutton = (Button) this.findViewById(R.id.registerbutton);

        // 회원가입 시 입력하는 공간..
        idText = (EditText) this.findViewById(R.id.idText);
        passText = (EditText) this.findViewById(R.id.passText);
        nameText = (EditText) this.findViewById(R.id.nameText);
        emailText = (EditText) this.findViewById(R.id.emailText);
        phoneText = (EditText) this.findViewById(R.id.phoneText);
        addr1Text = (EditText) this.findViewById(R.id.addr1Text);
        addr2Text = (EditText) this.findViewById(R.id.addr2Text);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                Bundle bundle = message.getData();
                String msg = bundle.getString("msg");
                int code=bundle.getInt("code");
                AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
                dialog = alert.setMessage(msg).setPositiveButton("확인", null).create();
                dialog.show();
                if(code!=0){
                    dialog.setOnDismissListener(e->{
                        finish();
                    });
                }
            }
        };

        // 회원가입 이벤트
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idtext = idText.getText().toString();
                String passtext = passText.getText().toString();
                String nametext = nameText.getText().toString();
                String emailtext = emailText.getText().toString();
                String phonetext = phoneText.getText().toString();
                String addr1text = addr1Text.getText().toString();
                String addr2text = addr2Text.getText().toString();

                Member member = new Member();
                member.setM_id(idtext);
                member.setM_pass(passtext);
                member.setM_name(nametext);
                member.setM_email(emailtext);
                member.setM_phone(phonetext);
                member.setM_addr1(addr1text);
                member.setM_addr2(addr2text);

                if (!check) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
                    dialog = alert.setMessage("중복체크를 먼저 해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if (idtext.equals("") || passtext.equals("") || nametext.equals("") || emailtext.equals("") || phonetext.equals("") || addr1text.equals("") || addr2text.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
                    dialog = alert.setMessage("빈칸없이 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                register(member);
            }
        });

        // 1. 아이디 중복체크
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idtext = idText.getText().toString();
                if (check) {
                    //return;

                }
                // 입력안했다면?
                if (idtext.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
                    dialog = alert.setMessage("아이디를 작성해주세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                check_ID(idtext);
            }
        });

        // 2. 다음 주소 API 사용
        if (btn_search != null) {
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(RegistActivity.this, WebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        addr1Text.setText(data);
                    }
                }
                break;
        }
    }

    //웹서버로부터 데이터베이스의 정보를 가져오자!! - 중복체크 / 회원가입
    public void check_ID(String m_id) {
        Thread thread = new Thread() {
            public void run() {
                BufferedReader buffr = null;
                try {
                    URL url = new URL("http://192.168.219.101:8888/rest/idCheck/" + m_id);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    buffr = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder(); //data값이 누적될 객체선언
                    String data = null;
                    while (true) {
                        data = buffr.readLine();
                        if (data == null) break;
                        sb.append(data);
                    }
                    Log.d(TAG, "------------------------------------------------------------------------------------------------"+sb.toString());
                    int code = con.getResponseCode(); //요청과 응답이 이루어짐..
                    Log.d(TAG,"code확인"+code);
                    //서버와 연결이 이미 끊긴 시점..
                    //서버로 부터 가져온 제이슨 배열만큼 이미지 로드 메서드를 호출!!!
                    try {
                        JSONObject json = new JSONObject(sb.toString());
                        int resultCode = json.getInt("resultCode");
                        if (resultCode == 0) {
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            String msg = "사용할 수 있는 아이디입니다.";
                            bundle.putString("msg",msg);
                            bundle.putInt("code",0);
                            message.setData(bundle);
                            handler.sendMessage(message);
                            check = true;
                        } else {
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            String msg = "사용할 수 없는 아이디입니다.";
                            bundle.putString("msg",msg);
                            bundle.putInt("code",0);
                            message.setData(bundle);
                            handler.sendMessage(message);
                            check = false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (buffr != null) {
                        try {
                            buffr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        thread.start(); //웹서버 요청 시작!!!
    }

    public void register(Member member) {
        Thread thread = new Thread() {
            public void run() {
                BufferedWriter buffw = null;
                try {
                    URL url = new URL("http://192.168.219.101:8888/rest/regist");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type","application/json;charset=utf-8");
                    con.setDoOutput(true);

                    buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
                    String jsonString = gson.toJson(member);
                    buffw.write(jsonString);
                    buffw.flush();
                    int resultCode = con.getResponseCode(); //요청과 응답이 이루어짐..

                    //서버와 연결이 이미 끊긴 시점..
                    //서버로 부터 가져온 제이슨 배열만큼 이미지 로드 메서드를 호출!!!
                    if (resultCode == 200) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        String msg = "회원가입에 성공하셨습니다.";
                        bundle.putString("msg",msg);
                        bundle.putInt("code",1);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        //check=false;

                    } else {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        String msg = "회원가입에 실패하였습니다.";
                        bundle.putString("msg",msg);
                        bundle.putInt("code",1);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        check=false;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (buffw != null) {
                        try {
                            buffw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        thread.start(); //웹서버 요청 시작!!!
    }
}
