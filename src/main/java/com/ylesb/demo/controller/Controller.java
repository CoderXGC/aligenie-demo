package com.ylesb.demo.controller;
/**
 * @title: Controller
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/98:24
 */

import com.alibaba.da.coin.ide.spi.meta.ExecuteCode;
import com.alibaba.da.coin.ide.spi.meta.ResultType;
import com.alibaba.da.coin.ide.spi.standard.ResultModel;
import com.alibaba.da.coin.ide.spi.standard.TaskQuery;
import com.alibaba.da.coin.ide.spi.standard.TaskResult;
import com.alibaba.da.coin.ide.spi.trans.MetaFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @className    : Controller
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/9 8:24]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/9 8:24]
 * @updateRemark : [描述说明本次修改内容] 
 */
@RestController
@Slf4j
public class Controller {
    @RequestMapping("/aligenie/faa08dd96136738ccb714acbe3fa71c2.txt")
    public String hello(){
        return "Jfc4Z4Ur15JwUBuvUQD5wg7Nu8+l";
    }
    @RequestMapping("/welcome")
    public ResultModel<TaskResult> taskResult(@RequestBody String json){
        // ResultModel<TaskResult> res = new ResultModel<>();
        log.info("json:{}",json);
        TaskQuery taskQuery =MetaFormat.parseToQuery(json);
        TaskResult taskResult = new TaskResult();
        // 从请求中获取意图参数以及参数值
        Map<String, String> paramMap = taskQuery.getSlotEntities().stream().collect(
                Collectors.toMap(slotItem -> slotItem.getIntentParameterName(),
                        slotItem -> slotItem.getOriginalValue()));
        //处理名称为 welcome 的意图
        if ("welcome".equals(taskQuery.getIntentName())) {
            taskResult.setReply("欢迎使用自定义技能~");
            log.info("json:{}",json);
            //处理名称为 weather 的意图
        }else {
            taskResult.setReply("请检查意图名称是否正确，或者新增的意图没有在代码里添加对应的处理分支。");
        }
        return reply(taskResult);
    }

    /**
     * 结束对话的回复，回复后音箱闭麦
     */
    public ResultModel<TaskResult> reply(TaskResult taskResult) {
        ResultModel<TaskResult> res = new ResultModel<>();
        taskResult.setExecuteCode(ExecuteCode.SUCCESS);
        taskResult.setResultType(ResultType.RESULT);
        res.setReturnCode("0");
        res.setReturnValue(taskResult);
        return res;
    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }
    // 根据Date时间生成UTC时间函数
    public static String generateUTCTime(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        dateFormat.setLenient(false);
        return dateFormat.format(time);
    }
}

