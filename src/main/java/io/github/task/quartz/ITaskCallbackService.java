package io.github.task.quartz;

/**
 * 类回调定时任务接口
 *
 * @author Created by 思伟 on 2020/8/24
 */
public interface ITaskCallbackService {

    /**
     * 类回调任务
     *
     * @param id          ID主键
     * @param jobName     任务名称
     * @param bizModule   业务模块
     * @param bizId       业务id
     * @param bizTag      业务标识
     * @param data        回调数据
     * @param callbackUrl 回调地址
     * @return boolean
     */
    boolean execute(Long id, String jobName,
                    String bizModule, String bizId, String bizTag, String data, String callbackUrl);

}
