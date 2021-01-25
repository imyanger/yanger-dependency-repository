package com.yanger.tools.web.tools;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangHao
 * @Description jwt工具类
 * @date 2018年9月23日-下午4:28:57
 */
public class JwtUtils {

    private static final String SECRET = "yanger#$%*!()<blog>";

    private static final String EXP = "exp";

    private static final String HEADER = "token";

    private static final Long expiresSecond = 1000L * 60 * 60 * 24;


    /**
     * 根据对象生成token
     *
     * @param object
     * @return
     * @author YangHao
     * @date 2018年9月23日-下午4:28:57
     */
    public static <T> String sign(T object) {
        return sign(object, expiresSecond);
    }

    /**
     * 根据对象生成token
     *
     * @param object        待生成的对象
     * @param expiresSecond 超时时间
     * @return
     * @author YangHao
     * @date 2018年9月23日-下午4:28:57
     */
    public static <T> String sign(T object, Long expiresSecond) {
        try {
            final JWTSigner signer = new JWTSigner(SECRET);
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(HEADER, jsonString);
            claims.put(EXP, System.currentTimeMillis() + expiresSecond);
            return signer.sign(claims);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token解析出对象
     *
     * @param jwt
     * @return
     * @author YangHao
     * @date 2018年9月23日-下午4:28:57
     */
    public static <T> T parse(Class<T> classT, String jwt) {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            final Map<String, Object> claims = verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(HEADER)) {
                long exp = (Long) claims.get(EXP);
                long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(HEADER);
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(json, classT);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description 获取token的时间
     * @Author yanger
     * @Date 2020/12/10 18:27
     * @param: jwt
     * @return: java.util.Date
     * @throws
     */
    public static Date getExpiration(String jwt) {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            final Map<String, Object> claims = verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(HEADER)) {
                long exp = (Long) claims.get(EXP);
                return new Date(exp);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断token是否过期
     *
     * @param jwt
     * @return true-过期
     * @author YangHao
     * @date 2018年9月23日-下午4:28:57
     */
    public static boolean isExpiration(String jwt) {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            final Map<String, Object> claims = verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(HEADER)) {
                long exp = (Long) claims.get(EXP);
                long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

}