package com.jie.backendModel.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.jie.backendModel.model.dto.question.JudgeCase;
import com.jie.backendModel.model.dto.question.JudgeConfig;
import com.jie.backendModel.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目表
 *
 * @TableName question
 */
@TableName(value = "question")
@Data
public class QuestionVO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;
    /**
     * 题目答案
     */
    private String answer;
    /**
     * 通过数
     */
    private Integer submitNum;
    /**
     * 提交数
     */
    private Integer acceptNum;
    /**
     * 判题用例（json数组）
     */
    private List<JudgeCase> judgeCase;
    /**
     * 判题配置（json对象）
     */
    private JudgeConfig judgeConfig;
    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人信息
     */
    private UserVO useVO;

    /**
     * 包装类转对象,用于存入数据库，将集合和字符串转为json字符串存入数据库
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        List<JudgeCase> judgeCaseList = questionVO.getJudgeCase();
        if (judgeCaseList != null) {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseList));
        }
        return question;
    }

    /**
     * 对象转包装类，用于与前端交互，把JSON字符串转换为集合和对象
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        System.out.println("________________");
        System.out.println(judgeConfigStr);
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        System.out.println(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        String judgeCaseStr = question.getJudgeCase();
        questionVO.setJudgeCase(JSONUtil.toList(judgeCaseStr, JudgeCase.class));
        return questionVO;
    }

}