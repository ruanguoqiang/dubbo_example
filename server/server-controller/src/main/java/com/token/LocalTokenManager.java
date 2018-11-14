package com.token;

import com.common.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalTokenManager extends TokenManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public Map<String,DummyUser> tokenMap=new ConcurrentHashMap<String, DummyUser>();

    public void verifyExpired() {
        Date now =new Date();
        for (Map.Entry<String,DummyUser> entry :tokenMap.entrySet()){
            if (now.compareTo(entry.getValue().expired)>0){
                tokenMap.remove(entry.getKey());
                logger.debug("token : " + entry.getKey() + "已失效");
            }
        }
    }

    public void addToken(String token, LoginUser loginUser) {
        DummyUser dummyUser = new DummyUser();
        dummyUser.loginUser = loginUser;
        extendExpiredTime(dummyUser);
        tokenMap.putIfAbsent(token, dummyUser);
    }

    public LoginUser validate(String token) {
        DummyUser dummyUser = tokenMap.get(token);
        if (dummyUser == null) {
            return null;
        }
        extendExpiredTime(dummyUser);
        return dummyUser.loginUser;
    }

    public void remove(String token) {
        tokenMap.remove(token);
    }

    /**
     * 扩展过期时间
     *
     * @param dummyUser
     */
    private void extendExpiredTime(DummyUser dummyUser) {
        dummyUser.expired = new Date(new Date().getTime() + tokenTimeout * 1000);
    }


    private class DummyUser {
        private LoginUser loginUser;
        private Date expired; // 过期时间
    }
}
