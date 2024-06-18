package com.jie.backendjudgeservice.judge;


import com.jie.backendModel.model.dto.quiestionsubmit.JudgeInfo;
import com.jie.backendModel.model.entity.QuestionSubmit;
import com.jie.backendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.jie.backendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.jie.backendjudgeservice.judge.strategy.JudgeContext;
import com.jie.backendjudgeservice.judge.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
