package yaksok.dodream.com.yaksok;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity {
    private String img_url;
    Button bt_chat,bt_InsertPill,btn_addFamily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        bt_chat = (Button)findViewById(R.id.bt_Chat);
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChattingMenu.class));
                overridePendingTransition(R.anim.pull_out_left,R.anim.pull_in_right);

            }
        });


       bt_InsertPill = (Button)findViewById(R.id.bt_InsertPill);
       bt_InsertPill.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),InsertPillActivity.class));
               overridePendingTransition(R.anim.pull_out_left,R.anim.pull_in_right);
           }
       });



       btn_addFamily = (Button)findViewById(R.id.bt_InsertFamily);
       btn_addFamily.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),AddYourFmaily.class));
               overridePendingTransition(R.anim.pull_out_left,R.anim.pull_in_right);
           }
       });
       }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.activity_customize_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.action_setting:
                Toast.makeText(getApplicationContext(),"눌림",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPageActivity.this,SettingPage.class);
                startActivity(intent);
                return true;
//                startActivity(new Intent(getApplicationContext(),SettingPage.class));
//                return  true;
        }
        return super.onOptionsItemSelected(menuItem);
    }





}
