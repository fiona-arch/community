package cn.tedu.community.provider;

import cn.tedu.community.dto.AccessTokenDTO;
import cn.tedu.community.dto.GitupUser;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitupProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
          String str=response.body().string();
          String []split=str.split("&");
          String tokenStr=split[0];
          String token=tokenStr.split("=")[1];
          System.out.println(token);
          return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public GitupUser getGitupUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str=response.body().string();
           GitupUser gitupUser=JSON.parseObject(str,GitupUser.class);
           return gitupUser;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}