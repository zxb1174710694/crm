package com.myCRM.web.listener;

import com.myCRM.Settings.Service.DicService;
import com.myCRM.Settings.Service.impl.DicServiceImpl;
import com.myCRM.Settings.domain.DicType;
import com.myCRM.Settings.domain.DicValue;
import org.apache.ibatis.session.SqlSessionFactory;
import com.myCRM.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class ApplicationListener implements ServletContextListener {

    //application启动时调用
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("application创建");
        //现在需要将数据字典的数据传入application的应用域当中，用于服务器缓存

        ServletContext application = event.getServletContext();

        //调用业务层对象，获取数据
        DicService target = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = target.getAll();

        //将业务层的数据拆解，放入应用域当中
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
            System.out.println("key:"+key+"数组长度："+map.get(key).size());
        }

        //将阶段和可能性的map存入application作用域

        Map<String,String> pMap = new HashMap<String, String>();

        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = bundle.getKeys();

        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = bundle.getString(key);

            pMap.put(key,value);
        }

        application.setAttribute("pMap",pMap);

        System.out.println("applicatiion作用域："+pMap.size());

    }

    //销毁时调用
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
