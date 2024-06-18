package com.jie.backendjudgeservice.judge.codesandbox;


import com.jie.backendModel.model.codesandbox.ExecuteCodeRequest;
import com.jie.backendModel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
