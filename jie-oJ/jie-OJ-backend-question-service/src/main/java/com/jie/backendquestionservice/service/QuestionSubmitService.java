package com.jie.backendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.backendModel.model.dto.quiestionsubmit.QuestionSubmitAddRequest;
import com.jie.backendModel.model.dto.quiestionsubmit.QuestionSubmitQueryRequest;
import com.jie.backendModel.model.entity.QuestionSubmit;
import com.jie.backendModel.model.entity.User;
import com.jie.backendModel.model.vo.QuestionSubmitVO;


/**
* @author 14316
* @description 针对表【question_submit(帖子点赞)】的数据库操作Service
* @createDate 2024-03-21 11:24:46
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param Loginuser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User Loginuser);

    /**
     * 分页获取帖子封装
     *
     * @param questionSubmitPage
     * @param Loginuser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,User Loginuser);


}
