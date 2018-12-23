package yaksok.dodream.com.yaksok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.adapter.MyRecyclerAdapter;
import yaksok.dodream.com.yaksok.item.RecyclerItem;
import yaksok.dodream.com.yaksok.service.MessageService;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.UserID2;
import yaksok.dodream.com.yaksok.vo.UserVO;
import yaksok.dodream.com.yaksok.vo.message.MessageBodyVO;
import yaksok.dodream.com.yaksok.vo.message.MessageResultBodyVO;
import yaksok.dodream.com.yaksok.vo.message.MessageVO;
import yaksok.dodream.com.yaksok.vo.message.SendMessageVO;

public class ChattingRoom extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Retrofit retrofit;
    private MessageService messageService;
    private EditText user_contextEdt;
    private String user_context;
    private String user2_id;
    private Button send_btn;
    TextView user1_txt,user2_txt,user_context2;
    ArrayList<RecyclerItem> recyclerItems;
    public static UserID2 userID2;
    public static String connectedName;


    ArrayList<SendMessageVO> albumList = new ArrayList<SendMessageVO>();
    public MyRecyclerAdapter myRecyclerAdapter;
    public LoginActivity login_activity;




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        final ArrayList<RecyclerItem> items = new ArrayList<>();

        initLayout();


        user_contextEdt = (EditText)findViewById(R.id.user_context_edt);
        user_contextEdt.setFocusable(true);
        user_context = user_contextEdt.getText().toString();
        send_btn = (Button)findViewById(R.id.send_btn);



        user1_txt = (TextView)findViewById(R.id.user1_txt);
        user2_txt = (TextView)findViewById(R.id.user2_txt);
        user_context2 = (TextView)findViewById(R.id.user_context);

        Intent intent = new Intent(getIntent());
        user2_id = intent.getStringExtra("user2");
        connectedName = intent.getStringExtra("name");

        userID2 = new UserID2(user2_id);


        retrofit = new Retrofit.Builder()
                .baseUrl(messageService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        messageService = retrofit.create(MessageService.class);
        Collections.reverse(albumList);
        getPreviouseConversation();


         myRecyclerAdapter = new MyRecyclerAdapter(albumList,R.layout.recycleritem);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inTime = new SimpleDateFormat("HHmmss", Locale.KOREA).format(new Date());
                String time = inTime.substring(0,2)+":"+inTime.substring(2,4);
                   Log.d("time","!!!!!!!!!!!!!"+inTime);

                SendMessageVO sendVO = new SendMessageVO();
                sendVO.setGivingUser(LoginActivity.userVO.getId());
                sendVO.setContent(user_contextEdt.getText().toString());
                sendVO.setReceivingUser(user2_id);
                sendVO.setRegidate(time);
                albumList.add(sendVO);

                Log.d("@@@@@@@@@@@"+"id",sendVO.getGivingUser()+"recive"+sendVO.getReceivingUser()+"context"+sendVO.getContent());
                Call<MessageResultBodyVO> call = messageService.sendAmeesage(sendVO);
                call.enqueue(new Callback<MessageResultBodyVO>() {
                    @Override
                    public void onResponse(Call<MessageResultBodyVO> call, Response<MessageResultBodyVO> response) {
                        MessageResultBodyVO messageBodyVO = response.body();
                        //201 : OK
                        //400: FCM error
                        //500 : Server Error
                        if(messageBodyVO.getStatus().equals("201")){
                            Toast.makeText(getApplicationContext(),"전송되었습니다.",Toast.LENGTH_SHORT).show();
                            mRecyclerView.setAdapter(myRecyclerAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            user_contextEdt.setText("");

                        }
                        else if(messageBodyVO.getStatus().equals("400")){
                            Toast.makeText(getApplicationContext(),"FCM Error",Toast.LENGTH_SHORT).show();
                        } else if (messageBodyVO.getStatus().equals("500")) {
                            Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResultBodyVO> call, Throwable t) {

                    }
                });

            }
        });

    }

    private void initLayout() {
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recyclerview);


    }

    public void getPreviouseConversation() {
        Call<MessageBodyVO> call = messageService.getTheChatting(LoginActivity.userVO.getId(),user2_id);
        call.enqueue(new Callback<MessageBodyVO>() {
            @Override
            public void onResponse(Call<MessageBodyVO> call, Response<MessageBodyVO> response) {
                MessageBodyVO bodyVO = response.body();
                //200 : OK
                //204 : 값없음(null반환)
                //500 : Server Error
                assert bodyVO != null;
               // Log.d("@@@@@@@@@@@@@@@@@@",bodyVO.getResult().get(0).getContent()+"id"+bodyVO.getResult().get(0).getGivingUser()+"id2"+bodyVO.getResult().get(0).getReceivingUser());
                //Toast.makeText(getApplicationContext(),"body"+bodyVO.getStatus()+"result"+bodyVO.getResult().size(),Toast.LENGTH_SHORT).show();

                assert bodyVO != null;
                if(bodyVO.getStatus().equals("200")){
                    for(int i = 0; i < bodyVO.getResult().size(); i++){
                    Log.d("실행","실행 됨");

                        SendMessageVO sendMessageVO = new SendMessageVO();
                        sendMessageVO.setGivingUser(bodyVO.getResult().get(i).getGivingUser());
                        sendMessageVO.setContent(bodyVO.getResult().get(i).getContent());
                        sendMessageVO.setReceivingUser(bodyVO.getResult().get(i).getReceivingUser());
                        sendMessageVO.setRegidate(bodyVO.getResult().get(i).getRegiDate().substring(11,16));

                        //Collections.reverse(albumList);//역순으로
                        albumList.add(sendMessageVO);
                    }
                    Collections.reverse(albumList);
                    mRecyclerView.setAdapter(new MyRecyclerAdapter(albumList,R.layout.recycleritem));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                }else if(bodyVO.getStatus().equals("204")){
                    user_contextEdt.setFocusable(true);
                }
                else if(bodyVO.getStatus().equals("500")){
                    Toast.makeText(getApplicationContext(),"서버 오류입니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessageBodyVO> call, Throwable t) {

            }
        });
    }
}
