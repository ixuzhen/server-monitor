package cn.luckynow.monitoringserver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.constants.RedisConstants;
import cn.luckynow.monitoringserver.entity.LoginUser;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.service.IMessageUserService;
import cn.luckynow.monitoringserver.service.IUserService;
import cn.luckynow.monitoringserver.service.LoginServcie;
import cn.luckynow.monitoringserver.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService iUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMessageUserService iMessageUserService;




    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        // TODO: 这里token有效期要变成永久的，目前是24消失
        String jwt = JwtUtil.createJWT(userId);

        // 存到 session 中
        //HttpSession session = request.getSession();
        //session.setAttribute(userId,loginUser);

        // 存到 application 当中
        //servletContext.setAttribute(userId,loginUser);

        // 存到 redis 中
        // 转成map
        //Map<String, Object> loginUserMap = BeanUtil.beanToMap(loginUser);
        // 转成 json
        String loginUserJson = JSONUtil.toJsonStr(loginUser);
        // 存到 redis 中
        String tokenKey = RedisConstants.LOGIN_USER_KEY + userId;
        stringRedisTemplate.opsForValue().set(tokenKey, loginUserJson );
        // 设置有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);


        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.successWithData(map);
    }

    @Value("${oauth.github.clientId}")
    private String githubClientId;

    @Value("${oauth.github.clientSecret}")
    private String githubClientSecret;

    @Value("${oauth.github.clientUrl}")
    private String githubClientUrl;

    @Value("${oauth.github.userInfoUrl}")
    private String githubUserInfoUrl;

    @Value("${oauth.gitee.clientId}")
    private String giteeClientId;

    @Value("${oauth.gitee.clientSecret}")
    private String giteeClientSecret;

    @Value("${oauth.gitee.clientUrl}")
    private String giteeClientUrl;

    @Value("${oauth.gitee.userInfoUrl}")
    private String giteeUserInfoUrl;


    @Override
    public Result loginGithub(String code) {
//        String baseUrl = "https://github.com/login/oauth/access_token";
        String url = githubClientUrl + "?client_id=" + githubClientId + "&client_secret=" + githubClientSecret + "&code=" + code;

        HttpResponse execute = HttpRequest.post(url)
                .header(Header.ACCEPT, "application/json")
                .execute();
        if (execute.isOk()) {
            String body = execute.body();
            JSONObject bodyJson = JSONUtil.parseObj(body);
            String accessToken = bodyJson.getStr("access_token");
//            String userInfoUrl = "https://api.github.com/user";
            HttpResponse userInfoResponse = HttpRequest.get(githubUserInfoUrl)
                    .header(Header.ACCEPT, "application/json")
                    .header(Header.AUTHORIZATION, "Bearer " + accessToken)
                    .execute();
            if (userInfoResponse.isOk()) {
                String userInfoBody = userInfoResponse.body();
                JSONObject userInfoBodyJson = JSONUtil.parseObj(userInfoBody);
                String login = userInfoBodyJson.getStr("login");
                Long idGithub = Long.parseLong(userInfoBodyJson.getStr("id"));

                List<User> userList = iUserService.getUserByGithubId(idGithub);
                if (userList.size() == 1) {
                    // 登录
                    User user = userList.get(0);
                    String jwt = getAndsaveJwt2Redis(user);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("token", jwt);
                    return Result.successWithData(map);
                } else if (userList.size() == 0) {
                    // 注册
                    String userName = "GitHub_" + idGithub + IdUtil.simpleUUID();
                    User user = new User();
                    user.setIdGithub(idGithub);
                    user.setUserName(userName);
                    return register(user);
                } else {
                    // 有问题
                    return Result.failed("GitHub id 出现问题");
                }

            }
        }
        return Result.failed("GitHub 登录失败");
    }

    @Override
    public Result loginGitee(String code) {
        String url = giteeClientUrl + "?grant_type=authorization_code&code=" + code + "&client_id=" + giteeClientId + "&redirect_uri=http://127.0.0.1:3000/login?oauth=gitee&client_secret=" + giteeClientSecret;
        HttpResponse execute = HttpRequest.post(url)
                .header(Header.ACCEPT, "application/json")
                .execute();
        if (execute.isOk()) {
            String body = execute.body();
            JSONObject bodyJson = JSONUtil.parseObj(body);
            String accessToken = bodyJson.getStr("access_token");
            HttpResponse userInfoResponse = HttpRequest.get(giteeUserInfoUrl + "?access_token=" + accessToken)
                    .header(Header.ACCEPT, "application/json")
                    .execute();
            if (userInfoResponse.isOk()) {
                String userInfoBody = userInfoResponse.body();
                JSONObject userInfoBodyJson = JSONUtil.parseObj(userInfoBody);
                String login = userInfoBodyJson.getStr("login");
                Long idGitee = Long.parseLong(userInfoBodyJson.getStr("id"));

                List<User> userList = iUserService.getUserByGiteeId(idGitee);
                if (userList.size() == 1) {
                    // 登录
                    User user = userList.get(0);
                    String jwt = getAndsaveJwt2Redis(user);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("token", jwt);
                    return Result.successWithData(map);
                } else if (userList.size() == 0) {
                    // 注册
                    String userName = "Gitee_" + idGitee + IdUtil.simpleUUID();
                    User user = new User();
                    user.setIdGitee(idGitee);
                    user.setUserName(userName);
                    return register(user);
                } else {
                    // 有问题
                    return Result.failed("Gitee id 出现问题");
                }

            }
        }
        return Result.failed("Gitee 登录失败");
    }


    // TODO: 退出登录要从redis中删除数据
    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + userId);
        return Result.successWithMessage("退出成功");
    }

    @Override
    public Result register(User user) {
        if (user.getPassword() != null) {
            String password = user.getPassword();
            password = passwordEncoder.encode(password);
            user.setPassword(password);
        }
        boolean success = iUserService.saveUser(user);
        if(!success)
            return Result.failed("注册失败！！");
        boolean success_message = iMessageUserService.saveMessageUser(user);
        if(!success_message)
            log.error("注册消息推送服务失败");

        String jwt = getAndsaveJwt2Redis(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.successWithData(map);
    }



    String getAndsaveJwt2Redis(User user) {
        String userId = user.getId().toString();

        LoginUser loginUser = new LoginUser(user);
        // 转成 json
        String loginUserJson = JSONUtil.toJsonStr(loginUser);
        // 存到 redis 中
        String tokenKey = RedisConstants.LOGIN_USER_KEY + userId;
        stringRedisTemplate.opsForValue().set(tokenKey, loginUserJson );
        // 设置有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        String jwt = JwtUtil.createJWT(userId);
        return jwt;
    }

}
