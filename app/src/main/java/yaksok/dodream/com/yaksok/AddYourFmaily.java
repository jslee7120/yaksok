package yaksok.dodream.com.yaksok;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.adapter.FamilyFindAdapter;
import yaksok.dodream.com.yaksok.item.FamilyItem;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.FamilyVO;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.FindFamilyVO;

public class AddYourFmaily extends AppCompatActivity {
    public EditText fmaily_number_edt;
    public Button family_find_btn, family_find_skip_btn,complete_btn;
    public ListView family_list_view;
    public Retrofit retrofit;
    public UserService userService;
    public FamilyFindAdapter adapter;
    public ArrayList<FamilyItem> familyItems;
    public  AlertDialog.Builder dialog;
    public static FamilyVO familyVO;
    public String family_user_id = "";
    public boolean isAddedFamily = false;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_your_fmailies);

        fmaily_number_edt = (EditText) findViewById(R.id.findFamily_edt);
        family_find_btn = (Button) findViewById(R.id.findFamily_btn);
        family_list_view = (ListView) findViewById(R.id.family_list);
        family_find_skip_btn = (Button) findViewById(R.id.family_skip_btn);
        complete_btn = (Button)findViewById(R.id.family_complete_btn);





         dialog = new AlertDialog.Builder(this);

        familyItems = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        adapter = new FamilyFindAdapter();

        family_find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<FindFamilyVO> findFamilyVOCall = userService.getUserList(fmaily_number_edt.getText().toString(), "phoneNumber");
                findFamilyVOCall.enqueue(new Callback<FindFamilyVO>() {
                    @Override
                    public void onResponse(Call<FindFamilyVO> call, Response<FindFamilyVO> response) {
                        FindFamilyVO findFamilyVO = response.body();

                        if (findFamilyVO.getStatus().equals("200")) {
                            isAddedFamily = true;
//                           Log.d("!!!!!!!!!!!!!!!!",findFamilyVO.getResult().get(1).getNickName()+""+findFamilyVO.getResult().get(0).getUserId());
//                            Toast.makeText(getApplicationContext(),findFamilyVO.getResult().get(0).getProfileImagePath(),Toast.LENGTH_LONG).show();

                            for (int i = 0; i < findFamilyVO.getResult().size(); i++) {
                                adapter.addItem(findFamilyVO.getResult().get(i).getNickName());
                                family_list_view.setAdapter(adapter);
                                familyVO = new FamilyVO();

                                // userVO.setId(sign_up_id_edt.getText().toString());
                                //        userVO.setPhoneNumber(sign_up_phone_number_edt.getText().toString());
                                //        userVO.setPw(sign_up_pw_edt.getText().toString());
                                //        userVO.setNickname(sign_up_name_edt.getText().toString());
                                //        userVO.setProfileImagePath("");
                                //        userVO.setEmail(user_email);
                                //        userVO.setBirthday(birthday);
                                //        userVO.setAgeRange("10-19");
                                //        userVO.setUserType("G");
                                family_user_id = findFamilyVO.getResult().get(i).getUserId();
                            }


//                              if(family_list_view != null){
////                                  family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                      @Override
////                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////
////                                                                        }


                        } else if (findFamilyVO.getStatus().equals("204")) {
                            Toast.makeText(getApplicationContext(), "상대의 계정이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else if (findFamilyVO.getStatus().equals("400")) {
                            Toast.makeText(getApplicationContext(), "잘못된 요청입니다.", Toast.LENGTH_LONG).show();
                        } else if (findFamilyVO.getStatus().equals("500")) {
                            Toast.makeText(getApplicationContext(), "서버 오루 입니다..", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FindFamilyVO> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });
//        if(isAddedFamily){
//
//        }
//        else{
//            Toast.makeText(getApplicationContext(),"가족을 추가 해주시거나 선택해주십시오",Toast.LENGTH_LONG).show();
//        }

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),LoginActivity.userVO.getId(),Toast.LENGTH_LONG).show();
                if(isAddedFamily){
                    startActivity(new Intent(getApplicationContext(),MainPageActivity.class));

                }
            }
        });
        family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((FamilyItem)adapter.getItem(position)).getName();
                Toast.makeText(AddYourFmaily.this, name+"이름이 맞습니까? ", Toast.LENGTH_SHORT).show();
                dialog.setTitle("가족찾기");
                dialog.setMessage(name+"을 가족으로 등록 하시겠습니까?");
                dialog.setCancelable(false);
                String user_id = "";

//                switch (LoginActivity.userType){
//                    case "G":
//                        user_id = SignUp.userVO.getId();
//                        break;
//                    case "K":
//                        user_id = String.valueOf(Kakao_User_Info.id);
//                        break;
//                    case "N":
//                        user_id = Naver_User_Info.getNaver_id();
//                        break;
//
//                }
                final String finalUser_id = user_id;
                dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      FamilyVO familyVO = new FamilyVO();
                      familyVO.setUser_1(LoginActivity.userVO.getId());
                      familyVO.setUser_2(family_user_id);
                        //code
                        //201 : OK
                        //403 : 삽입시 중복
                        //500 : Server Error
                        Call<BodyVO> call = userService.postRegisterFamily(familyVO);
                        call.enqueue(new Callback<BodyVO>() {
                            @Override
                            public void onResponse(Call<BodyVO> call, Response<BodyVO> response) {
                                BodyVO bodyVO = response.body();
                                assert bodyVO != null;
                                switch (bodyVO.getStatus()) {
                                    case "201":
                                        Toast.makeText(getApplicationContext(), "가족 추가가 되었습니다.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "403":
                                        Toast.makeText(getApplicationContext(), "삽입시 중복이 됩니다.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "500":
                                        Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_LONG).show();
                                        break;
                                }

                            }

                            @Override
                            public void onFailure(Call<BodyVO> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }


        });
    }

}




        //===전화번호를 사용하여 유저의 정보의 리스트를 가지고온다=====
//HTTP GET   users/{item} ? itemTyp
//ex) /01027250856 ? itemType=phoneNumber
//
//Host:  http://54.180.81.180:8080/users/010?itemType=phoneNumber
//
//request path variable  -  item
//request query paramter - itemType
//
//response
//BODY{
//	“status” : “code”
//	“result” : “UserVO” List
//
//}
//
//code
//200 : OK
//204: 계정 존재하지않음(null반환)
//400: 잘못된 요청(itemType)
//500 : Server Error








