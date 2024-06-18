package com.jie.backendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.jie.backendModel.model.codesandbox.ExecuteCodeRequest;
import com.jie.backendModel.model.codesandbox.ExecuteCodeResponse;
import com.jie.backendModel.model.dto.question.JudgeCase;
import com.jie.backendModel.model.dto.quiestionsubmit.JudgeInfo;
import com.jie.backendModel.model.entity.Question;
import com.jie.backendModel.model.entity.QuestionSubmit;
import com.jie.backendModel.model.enums.QuestionSubmitStatusEnum;
import com.jie.backendjudgeservice.judge.codesandbox.CodeSandbox;
import com.jie.backendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.jie.backendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.jie.backendjudgeservice.judge.strategy.JudgeContext;
import com.jie.backendserviceclient.service.QuestionFeignClient;
import com.jie.lgjbackendcommon.common.ErrorCode;
import com.jie.lgjbackendcommon.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;



    



    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.Witting.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().code(code).language(language).inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);


        List<String> outputList = executeCodeResponse.getOutputList();
        String message = executeCodeResponse.getMessage();
        JudgeInfo judgeInfoUpdate = new JudgeInfo();
        JudgeInfo judgeInfoResponse = executeCodeResponse.getJudgeInfo();


        Integer status = executeCodeResponse.getStatus();


        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);


        // 6）修改数据库中的判题结果
        questionSubmitUpdate.setId(questionSubmitId);
        judgeInfoUpdate.setMessage(message);
        if (status == null) {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        } else {
            switch (status) {
                case 1:
                    String judgeCase = question.getJudgeCase();
                    List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase,JudgeCase.class);
                    int size = judgeCases.size();
                    Boolean flag = false;
                    for (int i = 0; i < size; i++) {
                        JudgeCase judgeCase1 = judgeCases.get(i);
                        String output = outputList.get(i);
                        if (output.equals(judgeCase1.getOutput())) {
                            flag = true;
                        } else {
                           flag = false;
                        }
                    }
                    if(flag == true){
                        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
                    }else {
                        judgeInfoUpdate.setMessage("结果错误");
                        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
                    }
                    break;
                case 3:
                    questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
                    break;
            }
        }
        if (judgeInfoResponse != null) {
            judgeInfoUpdate.setMemory(judgeInfoResponse.getMemory());
            judgeInfoUpdate.setTime(judgeInfoResponse.getTime());
        }
        String jsonStr = JSONUtil.toJsonStr(judgeInfoUpdate);
        questionSubmitUpdate.setJudgeInfo(jsonStr);
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 7）返回判题结果
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
