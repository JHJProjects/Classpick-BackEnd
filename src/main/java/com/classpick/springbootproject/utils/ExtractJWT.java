package com.classpick.springbootproject.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction) {

        //JWT인 문자열을 갖게 되고 문자열 생성, Bearer 제거하고 빈 문자열 대체
        token.replace("Bearer ", "");

        //청크라는 배열의 집합, 기간에 이 토큰 분할
        //토큰을 헤더,페이로드,서명이 될 3개의 청크로 나눠주기
        String[] chunks = token.split("\\.");
        //JWT를 우리가 이해할 수 있는 정보로 해독하기
        Base64.Decoder decoder = Base64.getUrlDecoder();

        //페이로드만 1로 디코딩(해독)->JWT에 대한 정보가 있는 페이로드(사용에 있어서 전송되는 데이터)
        //청크의 배열 0은 헤더, 1은 페이로드, 2는 서명
        String payload = new String(decoder.decode(chunks[1]));

        // 페이로드를 ","로 분할해서 페이로드 내의 각 정보 얻기
        String[] entries = payload.split(",");
        //키 값 쌍이 필요하기 때문에 맵 생성
        Map<String, String> map = new HashMap<String, String>();

        //이메일이 될 sub의 값을 찾을 때까지 모든 항목 반복
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if(keyValue[0].equals(extraction)) {

                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove =2;
                }
                //키 값 1 포인트 길이에서 제거
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0], keyValue[1]);
            }
        }
        //map에 이 키가 포함되어 있으면 값 반환(이메일)
        if  (map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }

}
