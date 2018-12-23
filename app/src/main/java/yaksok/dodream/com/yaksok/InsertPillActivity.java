package yaksok.dodream.com.yaksok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import yaksok.dodream.com.yaksok.service.UserService;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.PillVO;

public class InsertPillActivity extends AppCompatActivity{

    Button bt_Insert, bt_QRScan;
    private IntentIntegrator qrScan;
    String qrResult, result_code, result_time;

    Retrofit retrofit;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertpill);
        bt_Insert = (Button)findViewById(R.id.bt_Insert);
        bt_QRScan = (Button)findViewById(R.id.bt_QRScan);

        qrScan = new IntentIntegrator(this);

        bt_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SearchPill.class);
//                startActivity(intent);
            }
        });

        //QR코드 스캔
        bt_QRScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("QR코드를 스캔하세요");
                qrScan.setOrientationLocked(false); //디폴트는 가로인데 세로일 경우 세로로 바꾸는 함
                qrScan.initiateScan();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(userService.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

    }


    //QR스캔후 정보를 파싱한다(해당 액티비티에서 QR스캐너 띄움)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(this, "스캔완료!", Toast.LENGTH_SHORT).show();
                System.out.println("@@@@@@@@@@@@@@@@@@@@Respone" + result.getContents());//  반환값 뭔지 확인
                qrResult = result.getContents();
//                try {
//                    //data를 json으로 변환
//                    JSONObject obj = new JSONObject(result.getContents());
//                    result_code = obj.getString("medicineNo");
//                    result_time = obj.getString("dosagi");
//                    System.out.println("@@@"+result_time + result_code);
//                } catch (JSONException e) {
//                    System.out.println("@@@@@@@@@@");
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        result_code = qrResult.substring(0,1);
        result_time = qrResult.substring(1);
        System.out.println("@@@@" + result_code + ":" + result_time);

        PillVO vo = new PillVO();
        System.out.println("@@@######"+UserInfo.getUserid());
        vo.setUserId(UserInfo.getUserid());
        vo.setMedicineNo(result_code);
        vo.setDosagi(result_time);
        Call<BodyVO> call = userService.postQRCode(vo);
        call.enqueue(new Callback<BodyVO>() {
            @Override
            public void onResponse(Call<BodyVO> call, Response<BodyVO> response) {
                BodyVO statusVO = response.body();
                System.out.println("############"+statusVO.getStatus());
                if(statusVO.getStatus().equals("201"))
                    Toast.makeText(getApplicationContext(), "등록 성공", Toast.LENGTH_LONG).show();
                else if(statusVO.getStatus().equals("403"))
                    Toast.makeText(getApplicationContext(), "약 중복", Toast.LENGTH_LONG).show();
                else if(statusVO.getStatus().equals("500"))
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BodyVO> call, Throwable t) {

            }
        });

    }


}
