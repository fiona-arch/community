package cn.tedu.community.controller;

import cn.tedu.community.dto.AccessTokenDTO;
import cn.tedu.community.dto.GitupUser;
import cn.tedu.community.provider.GitupProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GitupProvider gitupProvider;

    @Value("${githup.client.id}")
    private String clientId;
    @Value("${githup.client.secret}")
    private String clientSecret;
    @Value("${githup.redirect.uri}")
    private String redirectUri;


    @RequestMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken=gitupProvider.getAccessToken(accessTokenDTO);
        GitupUser user=gitupProvider.getGitupUser(accessToken);
        System.out.println(user.getName());
        return "index";

    }
}
