package io.github.service.impl;

import io.github.config.aop.service.BaseAopContext;
import io.github.frame.spring.IStartUp;
import io.github.task.quartz.ITaskCallbackService;
import io.github.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 类回调定时任务实现
 *
 * @author Created by 思伟 on 2020/8/24
 */
@Service
public class SysTaskCallbackServiceImpl extends BaseAopContext<SysTaskCallbackServiceImpl>
        implements ITaskCallbackService, IStartUp {

    @Resource
    private TaskExecutor taskExecutor;

    /**
     * 业务类回调定时任务实现
     */
    private List<IBizTaskCallbackService> bizTaskCallbackServices = new ArrayList<>(16);

    @Override
    public boolean execute(Long id, String jobName, String bizModule, String bizId,
                           String bizTag, String data, String callbackUrl) {
        try {
            logger.debug("类回调任务执行,bizModule={},bizId={},bizTag={}", bizModule, bizId, bizTag);
            for (IBizTaskCallbackService callbackService : bizTaskCallbackServices) {
                if (StringUtils.equals(callbackService.getBizModule(), bizModule)
                        && StringUtils.equals(callbackService.getBizTag(), bizTag)) {
                    // 业务类回调(当业务模块和业务标识一致时发起回调)
                    taskExecutor.execute(() -> {
                        // 休眠1s
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            callbackService.execute(jobName, bizId, data, callbackUrl);
                        } catch (Throwable e) {
                            // 原则上不处理业务方抛出的异常,此处暂不扩展
                            logger.warn("[请修改此处实现业务类回调服务的代码]业务类回调定时任务失败，e={}", e.getMessage());
                        }
                    });
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("类回调定时任务执行失败:{}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        final Map<String, IBizTaskCallbackService> beans =
                applicationContext.getBeansOfType(IBizTaskCallbackService.class);
        bizTaskCallbackServices.addAll(beans.values());
    }

    /**
     * 业务类回调定时任务接口
     */
    public interface IBizTaskCallbackService {

        /**
         * 设置回调的业务模块
         *
         * @return String
         */
        String getBizModule();

        /**
         * 设置回调的业务标识
         *
         * @return String
         */
        String getBizTag();

        /**
         * 回调执行
         *
         * @param jobName     任务名称
         * @param bizId       业务id
         * @param data        回调数据
         * @param callbackUrl 回调地址
         */
        void execute(String jobName, String bizId, String data, String callbackUrl);

    }

}
