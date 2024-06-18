package com.jie.backendModel.model.dto.question;

import lombok.Data;

@Data
public class JudgeConfig {

    /**
     * 时间限(ms)
     */
    private Integer timeLimit;
    /**
     * 内存限制(KB)
     */
    private Integer memoryLimit;

    /**
     * 堆栈限制(KB)
     */
    private Integer stackLimit;
}
