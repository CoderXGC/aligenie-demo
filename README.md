![image.png](https://ucc.alicdn.com/pic/developer-ecology/e01352914fcf43ecaf8fd4f1d4447e62.png)
### 🔥  介绍

最终效果可以观看视频：https://h5.pipix.com/s/NTHB9gk/
### 💥  项目结构

![image.png](https://ucc.alicdn.com/pic/developer-ecology/1c5696dd4aef487f9cf8685a87346bbf.png)
### 🔥  相关技术springboot

使用框架springboot，快速搭建后端服务。  
###🔥  搭建教程
打开天猫精灵技能官网：https://iap.aligenie.com/
![image.png](https://ucc.alicdn.com/pic/developer-ecology/4d3b8977ac6d433ab26d00e3f1b338e2.png)
然后选择私域技能：  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/73fea72fda114d5b8ff723f22685727e.png)
语音交互模型，虽然我们无调用词，因为无调用词，需要获取你的设备openid，后面会介绍到。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/3bf3bcf0778643bbb7cb72c8d88ef8a1.png)
点击创建一个意图。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/60c90f2732184c9c8c63725d3c365500.png)
![image.png](https://ucc.alicdn.com/pic/developer-ecology/967176c9065641f6baf05dea599a831a.png)
这里一定要设置默认意图。一会调用就可以获取到我们无调用词的相关数据了。  
服务部署。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/1f60de1c3f074a82862e65b5b534145e.png)
![image.png](https://ucc.alicdn.com/pic/developer-ecology/693ccebca4434aa2b3bd917732d3ebcb.png)
其实这里就是验证这个服务器是你的。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/43993dab7fce4778a02ca0788ea2fef1.png)
下载认证数据。

![image.png](https://ucc.alicdn.com/pic/developer-ecology/da69f25d9867432a94439e588ad862a3.png)
这就是添加好了的。  
如果要使用推送功能必须要申请这个权限。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/657ea84a268244ff8e74ce981d858a34.png)
下面就是申请推送语音播报的模板了。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/f01acb700f264b6698407ec0bd2ca0a9.png)
点击新建，可以根据个人需要进行申请。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/453025e745134753af1e28f2e4703277.png)
![image.png](https://ucc.alicdn.com/pic/developer-ecology/ac27d7d5e22c42f19bf9b6deb9ede101.png)
下面就是测试了。我们开始搭建服务端。  
其实这个就回到了最初的哪个问题，我们是无调用词的，怎么触发服务端的代码呢？如何获取一些我们需要的东西呢？  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/28e1dce4056f4da4b0b665c74b0fb029.png)
我们在编辑界面，设置一下调用词  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/74c48598becb4dcba045cc819fd36319.png)
然后写好代码，然后上传服务器。
```js
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
```  
因为我们需要相关id，我们需要在测试界面，进行真机测试  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/0fbaf4781c864eaea7e9865e85fb742c.png)
实际调用，可以看到我们需要的数据，我们可以用到。  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/274ecd65615649c8998fb7f389773207.png)
### 🔥实现自动推送

打开自动推送:  
https://open-api.aligenie.com/?version=iap_1.0&apiName=PushNotifications  
![image.png](https://ucc.alicdn.com/pic/developer-ecology/5698bdb0876a4a45be4b4612cef8ec9b.png)
可以看到我们需要这些参数，最重要的是刚才我们在服务端获取的。  
DEVICE_UNION_ID ：设备unionId  
DEVICE_OPEN_ID ：设备openId  
USER_UNION_ID ：用户unionId  
USER_OPEN_ID ：用户openId  
这些值。我们需要用到的。剩下的跟着官方教程就可以完成了。  
## 交流
![Image text](https://i.loli.net/2021/11/29/Rm1SX7JWPBEDsat.png)
### 捐助  
![Image text](https://www.ylesb.com/wp-content/uploads/2022/04/1651062390-642.png)
