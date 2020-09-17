package io.github.frame.prj.service;

import io.github.common.enums.IEnum;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.model.BillActor;
import io.github.frame.prj.model.KvObject;
import io.github.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 页面数据缓存
 *
 * @author Created by 思伟 on 2020/8/28
 */
@Slf4j
public class CacheHandler {

    /**
     * 定时任务回调类型
     */
    public static final String SYS_TASK_CALLBACK_TYPE_LIST = "sysTaskCallbackTypeList";
    /**
     * 缓存操作
     */
    private static CacheHandler cacheHandler;

    /**
     * 外部不提供实例化方法
     */
    private CacheHandler() {
    }

    /**
     * 获取唯一实例对象
     */
    public static CacheHandler getInstance() {
        if (null == cacheHandler) {
            synchronized (CacheHandler.class) {
                if (null == cacheHandler) {
                    cacheHandler = new CacheHandler();
                }
            }
        }
        return cacheHandler;
    }

    /**
     * 加载缓存
     *
     * @param model      视图模型
     * @param billActor  操作人信息
     * @param cacheNames 缓存名
     */
    public void loadCache(Model model, BillActor billActor, String... cacheNames) {
        if (ArrayUtils.isEmpty(cacheNames)) {
            return;
        }
        for (String cacheName : cacheNames) {
            if (SYS_TASK_CALLBACK_TYPE_LIST.equals(cacheName)) {
                loadSysTaskCallbackTypeList(model, billActor);
            } else {
                log.warn("Error cacheName[{}] ,Can't Resolve it", cacheName);
            }
        }
    }

    private void loadSysTaskCallbackTypeList(Model model, BillActor billActor) {
        model.addAttribute(SYS_TASK_CALLBACK_TYPE_LIST, generateEnum2KVList(TaskCallbackTypeEnum.values()));
    }

    /**
     * 根据枚举生成KV集合
     *
     * @param enums 枚举
     * @return List
     */
    private List<KvObject> generateEnum2KVList(IEnum... enums) {
        List<KvObject> list = new ArrayList<>();
        KvObject kv;
        for (IEnum anEnum : enums) {
            kv = new KvObject(anEnum.getKey(), anEnum.getValue());
            list.add(kv);
        }
        return list;
    }

    /**
     * 根据常量键值对生成KV集合
     *
     * @param constMap 常量键值对
     * @return List
     */
    private List<KvObject> generateMap2KVList(Map<String, ?> constMap) {
        List<KvObject> list = new ArrayList<KvObject>();
        if (null != constMap) {
            Set<? extends Map.Entry<String, ?>> entrySet = constMap.entrySet();
            KvObject kv;
            for (Map.Entry<String, ?> entry : entrySet) {
                kv = new KvObject(entry.getKey(), StringUtils.toString(entry.getValue()));
                list.add(kv);
            }
        }
        return list;
    }

}
