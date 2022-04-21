package com.ylesb.demo.utils;
/**
 * @title: Client
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/1811:01
 */


import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * @className    : Client
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/18 11:01]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/18 11:01]
 * @updateRemark : [描述说明本次修改内容] 
 */

public class Client {

    public final static String SEPARATOR = "&";
    public final static String URL_ENCODING = "UTF-8";
    public static final String SIGN_ALGORITHM = "ACS3-HMAC-SHA256";
    public static final String HASH_SHA256 = "SHA-256";
    public static final String HASH_SM3 = "SM3";
    public static final String HMAC_SHA256 = "ACS3-HMAC-SHA256";
    public static final String RSA_SHA256 = "ACS3-RSA-SHA256";
    public static final String HMAC_SM3 = "ACS3-HMAC-SM3";
    public static final String UTF8 = "UTF-8";
    public static final String PEM_BEGIN = "-----BEGIN RSA PRIVATE KEY-----\n";
    public static final String PEM_END = "\n-----END RSA PRIVATE KEY-----";

    protected static Map<String, String> getCanonicalizedHeadersMap(Map<String, String> headers) {
        Map<String, String> result = new HashMap<>();
        if (headers == null) {
            return result;
        }
        String prefix = "x-acs-";
        Set<String> keys = headers.keySet();
        List<String> canonicalizedKeys = new ArrayList<>();
        Map<String, String> valueMap = new HashMap<>();
        for (String key : keys) {
            String lowerKey = key.toLowerCase();
            if (lowerKey.startsWith(prefix) || lowerKey.equals("host")
                    || lowerKey.equals("content-type")) {
                if (!canonicalizedKeys.contains(lowerKey)) {
                    canonicalizedKeys.add(lowerKey);
                }
                valueMap.put(lowerKey, headers.get(key).trim());
            }
        }
        String[] canonicalizedKeysArray = canonicalizedKeys.toArray(new String[canonicalizedKeys.size()]);
        String signedHeaders = StringUtils.join(Arrays.asList(canonicalizedKeysArray), ";");
        Arrays.sort(canonicalizedKeysArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canonicalizedKeysArray.length; i++) {
            String key = canonicalizedKeysArray[i];
            sb.append(key);
            sb.append(":");
            sb.append(valueMap.get(key));
            sb.append("\n");
        }
        result.put("canonicalHeaders", sb.toString());
        result.put("signedHeaders", signedHeaders);
        return result;
    }

    protected static String getCanonicalizedQueryString(StringBuilder sb, Map<String, String> query, String[] keys) throws Exception {
        if (query == null || query.size() == 0) {
            return "";
        }
        if (keys == null || keys.length == 0) {
            return "";
        }
        if (sb == null) {
            sb = new StringBuilder();
        }
        Arrays.sort(keys);
        String key;
        String value;
        for (int i = 0; i < keys.length; i++) {
            key = keys[i];
            sb.append(percentEncode(key));
            value = query.get(key);
            sb.append("=");
            if (!StringUtils.isEmpty(value)) {
                sb.append(percentEncode(value));
            }
            sb.append(SEPARATOR);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    protected static String getCanonicalizedResource(Map<String, String> query) throws Exception {
        if (query == null || query.size() == 0) {
            return "";
        }
        String[] keys = query.keySet().toArray(new String[query.size()]);
        StringBuilder result = new StringBuilder();
        return getCanonicalizedQueryString(result, query, keys);
    }

    public static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, URL_ENCODING).replace("+", "%20")
                .replace("*", "%2A").replace("%7E", "~") : null;
    }

    public static String hexEncode(byte[] raw) {
        if (raw == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            String hex = Integer.toHexString(raw[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] hash(byte[] raw, String signAlgorithm) throws Exception {
        if (signAlgorithm == null) {
            return null;
        }
        if (signAlgorithm.equals(HMAC_SHA256) || signAlgorithm.equals(RSA_SHA256)) {
            MessageDigest digest = MessageDigest.getInstance(HASH_SHA256);
            return digest.digest(raw);
        } else if (signAlgorithm.equals(HMAC_SM3)) {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            MessageDigest digest = MessageDigest.getInstance(HASH_SM3, provider);
            return digest.digest(raw);
        }
        return null;
    }


    public static String getAuthorization(TeaRequest request, String payload, String accessKey, String secret) throws Exception {
        if (request == null) {
            return null;
        }
        if (secret == null) {
            throw new Exception("Need secret!");
        }
        String canonicalURI = request.pathname;
        if (canonicalURI == null || StringUtils.isEmpty(canonicalURI) || "".equals(canonicalURI.trim())) {
            canonicalURI = "/";
        }
        String method = request.method;
        Map<String, String> headers = request.headers;
        Map<String, String> query = request.query;
        Map<String, String> cannoicalHeaders = getCanonicalizedHeadersMap(headers);
        String signedHeaders = cannoicalHeaders.get("signedHeaders");
        String queryString = getCanonicalizedResource(query);
        StringBuilder sb = new StringBuilder(method);

        String hashPayload = hexEncode(hash(payload.getBytes(UTF8), SIGN_ALGORITHM));

        sb.append("\n").append(canonicalURI).append("\n").append(queryString).append("\n").append(cannoicalHeaders.get("canonicalHeaders"))
                .append("\n").append(signedHeaders).append("\n").append(hashPayload);
        String hex = hexEncode(hash(sb.toString().getBytes(UTF8), SIGN_ALGORITHM));
        String stringToSign = SIGN_ALGORITHM + "\n" + hex;
        String signature = hexEncode(SignatureMethod(stringToSign, secret, SIGN_ALGORITHM));
        String auth = SIGN_ALGORITHM + " Credential=" + accessKey + ",SignedHeaders=" + signedHeaders
                + ",Signature=" + signature;
        return auth;
    }

    protected static String checkRSASecret(String secret) {
        if (secret != null) {
            if (secret.startsWith(PEM_BEGIN)) {
                secret = secret.replace(PEM_BEGIN, "");
            }
            while (secret.endsWith("\n") || secret.endsWith("\r")) {
                secret = secret.substring(0, secret.length() - 1);
            }
            if (secret.endsWith(PEM_END)) {
                secret = secret.replace(PEM_END, "");
            }
        }
        return secret;
    }

    public static byte[] SignatureMethod(String stringToSign, String secret, String signAlgorithm) throws Exception {
        if (stringToSign == null || secret == null || signAlgorithm == null) {
            return null;
        }
        byte[] bytes = null;
        if (signAlgorithm.equals(HMAC_SHA256)) {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            bytes = sha256_HMAC.doFinal(stringToSign.getBytes());
            return bytes;
        } else if (signAlgorithm.equals(RSA_SHA256)) {
            secret = checkRSASecret(secret);
            Signature rsaSign = Signature.getInstance("SHA256withRSA");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] keySpec = new BASE64Decoder().decodeBuffer(secret);
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(keySpec));
            rsaSign.initSign(privateKey);
            rsaSign.update(stringToSign.getBytes(UTF8));
            bytes = rsaSign.sign();
        } else if (signAlgorithm.equals(HMAC_SM3)) {
            SecretKey key = new SecretKeySpec((secret).getBytes(UTF8), "HMAC-SM3");
            HMac mac = new HMac(new SM3Digest());
            bytes = new byte[mac.getMacSize()];
            byte[] inputBytes = stringToSign.getBytes(UTF8);
            mac.init(new KeyParameter(key.getEncoded()));
            mac.update(inputBytes, 0, inputBytes.length);
            mac.doFinal(bytes, 0);
        }
        return bytes;
    }
}