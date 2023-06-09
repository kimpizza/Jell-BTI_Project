package my.jelly.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.jelly.entity.Member;
import my.jelly.service.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@RestController
@Log4j2
public class KakaoController {

    @Autowired
    KakaoService kakaoService;

    /* (카카오 로그인) */

    // 프론트에서 인가코드를 받아옴, 받은 인가코드로 카카오서버에서 액세스 토큰 받아와서 반환
    // http://localhost:4000/oauth/login/kakao
    @RequestMapping("/oauth/login/kakao")
    public String login(@RequestParam(value = "code", required = false) String code) throws Exception {
        System.out.println(code);
        String access_token = kakaoService.getToken(code);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return access_token;
    }

    // 프론트에서 받은 액세스 토큰으로 카카오서버에 요청해 유저 정보 가져오고, 우리 DB에 있는지 확인, 그리고 로그인/회원가입 처리
    @RequestMapping("/oauth/login/userInfo")
    public String userInfo(@RequestParam(value = "token") String token,
                           HttpServletRequest request)
            throws Exception {
        Member userInfo = kakaoService.getUserInfo(token);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> map = new HashMap<String, Object>();

        if(userInfo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", userInfo);
            map.put("userInfo", userInfo);

            //로그인 성공 확인용
            log.info("로그인 성공!!!, userinfo={}", userInfo);
            return gson.toJson(map);
        }


        return "로그인에 실패하였습니다!";
    }

}

