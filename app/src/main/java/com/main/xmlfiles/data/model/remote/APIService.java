package com.main.xmlfiles.data.model.remote;

import com.google.gson.JsonArray;
import com.main.xmlfiles.data.model.Auctions;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.Logg;
import com.main.xmlfiles.data.model.Messaging_fragments.Messages;
import com.main.xmlfiles.data.model.MyAuctions;
import com.main.xmlfiles.data.model.Users;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    @GET("/auth/login/{user_id}/{password}")
    Call<Users> getAnswers(@Path("user_id") String user,@Path("password") String password);

    @GET("/auth/b")
    Call<Users> getA();

    @Headers({
            "Content-Type:application/json"
    })
    @POST("auctions/create_auction")
    Call<Auctions> getAuctions(@Body Auctions auction);

    @GET("auctions/start_auction/{user_id}/{auction_id}")
    Call<String> startAuction(@Path("user_id") String user_id,@Path("auction_id") Integer auction_id);

    @Headers({
            "Content-Type:application/json"
    })
    @POST("items/create_item")
    Call<Items> createItem(@Body Items item);

    @Multipart
    @POST("items/add_pictures")
    Call<Auctions> addPicture(@Part MultipartBody.Part file);

    @GET("chat/showincomes/{receiver}")
    Call<List<Messages>> showincomes(@Path("receiver") String receiver);

    @GET("chat/showoutcomes/{sender}")
    Call<List<Messages>> showoutcomes(@Path("sender") String sender);

    @GET("chat/deletemsg/{receiver}/{message_id}")
    Call<String> deleteMessage(@Path("receiver") String receiver, @Path("message_id") Integer message_id);

    @GET("chat/readmsg/{sender}/{receiver}/{message_id}")
    Call<String> readMessage(@Path("sender") String sender,@Path("receiver") String receiver, @Path("message_id") Integer message_id);

    @GET("chat/sendmsg/{sender}/{receiver}/{message}")
    Call<String> sendMessage(@Path("sender") String sender,@Path("receiver") String receiver, @Path("message") String message_id);

    @GET("chat/getunreadmessage/{receiver}")
    Call<Integer> getUnreadMessages(@Path("receiver") String receiver);

    //search
    //@GET("/search/searchbytag/{tag}")
    //Call<List<MyAuctions>> searchauctionsbytag(@Path("tag") String tag);

    //@GET("/search/searchbytagdescription/{tag}/{name}")
    //Call<List<MyAuctions>> searchAuctionByTagDescr(@Path("tag") String tag, @Path("name") String name);

    @GET("/search/searchauctions/{word}")
    Call<JsonArray> searchauctionsbyword(@Path("word") String word);

    @Headers({
            "Content-Type:application/json"
    })
    @GET("auctions/my_auctions/{user_id}")
    Call<JsonArray> getMyAuctions(@Path("user_id") String user_id);

    //deleteAuction

    @GET("auctions/deleteauction/{user_id}/{auction_id}")
    Call<String> deleteAuction(@Path("user_id") String user_id, @Path("auction_id") Integer auction_id);

    //foreach auction
    //@GET("/auctions/my_auctions/{user_id}")
    //Call<List<Auctions>> getMyAuctions(@Path("user_id") String user);

    @GET("/items/get_auction_items/{auction_id}")
    Call<List<Items>> getAuctionItems(@Path("auction_id") Integer auction_id);

    @GET("/items/get_item_pic_count/{item_id}")
    Call<Auctions> getPicsItems(@Path("item_id") Integer item_id);

    //bid
    @GET("/bids/make_bid/{auction_id}/{bidder_id}/{bid}")
    Call<String> makeBid(@Path("auction_id") Integer auction_id,@Path("bidder_id") String bidder_id,@Path("bid") Double bid);

    //votes
    @POST("/vote/voteseller/{voter_id}/{candidate_id}/{type_vote}")
    Call<String> upvoteSeller(@Path("voter_id") String voter_id,@Path("candidate_id") String candidate_id,@Path("type_vote") Boolean type_vote);

    @POST("/vote/votebidder/{voter_id}/{candidate_id}/{type_vote}")
    Call<String> upvoteBidder(@Path("voter_id") String voter_id,@Path("candidate_id") String candidate_id,@Path("type_vote") Boolean type_vote);
    /*@POST("/auth/register")
    @FormUrlEncoded
    Call<String> savePost(@Field("user_id") String user_id,@Field("name") String name,@Field("surname") String surname,@Field("password") String password,
                          @Field("country") String country,@Field("afm") Integer afm
    ,@Field("email") String email,@Field("city") String city,@Field("telephone") String telephone,@Field("address") String address);*/
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/auth/register")
    Call<String> savePost(@Body Users users);

    //logg
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/search/log")
    Call<Logg> insertLog(@Body Logg logg);

    //recommendation
    @GET("/rec/get_rec/{user_id}")
    Call<List<Items>> recommend(@Path("user_id") String user_id);

    @GET("/rec/get_auction/{auction_id}")
    Call<JsonArray> getrecommendAdditianlData(@Path("auction_id") Integer auction_id);
}
