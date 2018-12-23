package yaksok.dodream.com.yaksok.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import yaksok.dodream.com.yaksok.vo.ConnectedFamilyVO;
import yaksok.dodream.com.yaksok.vo.FamilyVO;
import yaksok.dodream.com.yaksok.vo.BodyVO;
import yaksok.dodream.com.yaksok.vo.FcmTokenVO;
import yaksok.dodream.com.yaksok.vo.FindFamilyVO;
import yaksok.dodream.com.yaksok.vo.PillVO;
import yaksok.dodream.com.yaksok.vo.SnsBodyVO;
import yaksok.dodream.com.yaksok.vo.SnsLogVO;
import yaksok.dodream.com.yaksok.vo.UserVO;

public interface UserService {
    public static final String API_URL = "http://54.180.81.180:8080";

    @POST("/users")
    Call<BodyVO>postSignUp(@Body UserVO userVO);

    @POST("/users/login")
    Call<BodyVO>postSnsLogin(@Body UserVO userVO);

    @POST("/users/login")
    Call<BodyVO>postGneralLogin(@Body UserVO userVO);

    @POST("mymedicines/")
    Call<BodyVO> postQRCode(@Body PillVO pillVO);

    @GET("/users/{item}")
    Call<FindFamilyVO> getUserList(@Path("item") String item, @Query("itemType") String itemType);

    @POST("families/")
    Call<BodyVO>postRegisterFamily(@Body FamilyVO familyVO);

    @GET("/families/{userId}")
    Call<FindFamilyVO> getConnectedFamilyInfo(@Path("userId") String userId);

    @PUT("/users/fcmtokens")
    Call<BodyVO> putToken(@Body FcmTokenVO fcmTokenVO);


    //=====토큰 업데이트======
    //HTTP PUT   /users/fcmtokens
    //Host:  http://54.180.81.180:8080
    //
    //request
    //BODY{
    //	“id” : string
    //	“fcmToken” : string
    //}
    //
    //
    //response
    //BODY{
    //	“status” : “code”
    //}
    //
    //code
    //201 : OK
    //500 : Server Error

}
