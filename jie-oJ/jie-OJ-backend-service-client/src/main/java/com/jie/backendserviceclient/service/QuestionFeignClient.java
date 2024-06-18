package com.jie.backendserviceclient.service;


import com.jie.backendModel.model.entity.Question;
import com.jie.backendModel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;


@FeignClient(name = "jie-OJ-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * 通过id获取题目
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
   Question getQuestionById(@RequestParam("questionId") Long questionId);

    /**
     * 通过id获取题目提交
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
   QuestionSubmit getQuestionSubmitById(Long questionSubmitId);

    /**
     * 更新题目
     * @param questionSubmitUpdate
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);



}
