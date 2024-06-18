package com.jie.backendModel.model.dto.quiestionsubmit;


import com.jie.lgjbackendcommon.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 * */
@Data
public class  QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;
    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 用户id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;

}