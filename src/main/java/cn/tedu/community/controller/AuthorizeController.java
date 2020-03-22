package cn.tedu.community.controller;

import cn.tedu.community.dto.AccessTokenDTO;
import cn.tedu.community.dto.GitupUser;
import cn.tedu.community.provider.GitupProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GitupProvider gitupProvider;


    @RequestMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("c88e822b0d957ee2db35");
        accessTokenDTO.setClient_secret("78e5e7777eda49cb051555928c8669f7923d4457");
        String accessToken=gitupProvider.getAccessToken(accessTokenDTO);
        GitupUser user=gitupProvider.getGitupUser(accessToken);
        System.out.println(user.getName());
        return "index";

    }
}
