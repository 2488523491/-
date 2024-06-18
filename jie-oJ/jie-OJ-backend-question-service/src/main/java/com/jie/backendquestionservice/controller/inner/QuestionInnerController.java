package com.jie.backendquestionservice.controller.inner;

import com.jie.backendModel.model.entity.Question;
import com.jie.backendModel.model.entity.QuestionSubmit;
import com.jie.backendquestionservice.service.QuestionService;
import com.jie.backendquestionservice.service.QuestionSubmitService;
import com.jie.backendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    /**
     * 通过id获取题目
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") Long questionId){
        return questionService.getById(questionId);
    }

    /**
     * 通过id获取题目提交
     * @param questionSubmitId
     * @return
     */
    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(Long questionSubmitId){
       return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 更新题目
     * @param questionSubmitUpdate
     * @return
     */
    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate){
        return questionSubmitService.updateById(questionSubmitUpdate);
    }
}
