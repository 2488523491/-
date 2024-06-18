package com.jie.backendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.jie.backendModel.model.codesandbox.ExecuteCodeRequest;
import com.jie.backendModel.model.codesandbox.ExecuteCodeResponse;
import com.jie.backendjudgeservice.judge.codesandbox.CodeSandbox;
import com.jie.lgjbackendcommon.common.ErrorCode;
import com.jie.lgjbackendcommon.exception.BusinessException;


/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8078/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);


        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        System.out.println("-----------------------------------------");
        System.out.println(responseStr);
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }

}

