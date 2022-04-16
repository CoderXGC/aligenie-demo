package com.ylesb.demo.utils;
/**
 * @title: TeaRequest
 * @projectName demo
 * @description: TODO
 * @author White
 * @site : [www.ylesb.com]
 * @date 2022/4/159:10
 */

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @className    : TeaRequest
 * @description  : [描述说明该类的功能]  
 * @author       : [XuGuangchao]
 * @site         : [www.ylesb.com]
 * @version      : [v1.0]
 * @createTime   : [2022/4/15 9:10]
 * @updateUser   : [XuGuangchao]
 * @updateTime   : [2022/4/15 9:10]
 * @updateRemark : [描述说明本次修改内容] 
 */

public class TeaRequest {
    public String method = "GET";
    public String pathname;
    public Map<String, String> query = new HashMap();
    public Map<String, String> headers = new HashMap();
    public InputStream body;

    public TeaRequest() {
    }


}
