package com.jie.backendjudgeservice.controller.inner;

import com.jie.backendModel.model.entity.QuestionSubmit;
import com.jie.backendjudgeservice.judge.JudgeService;
import com.jie.backendserviceclient.service.JudgeFeignClent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("inner")
public class JudgeInnerController implements JudgeFeignClent {

    @Resource
    private JudgeService judgeService;
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/doJudge")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}
