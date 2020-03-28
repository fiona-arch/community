package cn.tedu.community.controller;

import cn.tedu.community.dto.AccessTokenDTO;
import cn.tedu.community.dto.GitupUser;
import cn.tedu.community.mapper.UserMapper;
import cn.tedu.community.model.User;
import cn.tedu.community.provider.GitupProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitupProvider gitupProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${githup.client.id}")
    private String clientId;
    @Value("${githup.client.secret}")
    private String clientSecret;
    @Value("${githup.redirect.uri}")
    private String redirectUri;


    @RequestMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken=gitupProvider.getAccessToken(accessTokenDTO);
        GitupUser gitupUser=gitupProvider.getGitupUser(accessToken);
        if(gitupUser!=null){
            User user=new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(gitupUser.getName());
            user.setAccountId(String.valueOf(gitupUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
            //登陆失败  重新登录
            return "redirect:/";
        }

    }
}
