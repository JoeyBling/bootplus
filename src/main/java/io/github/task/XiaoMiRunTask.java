package io.github.task;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.frame.log.LogUtil;
import io.github.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 小米运动刷步数-定时任务
 *
 * @author Created by 思伟 on 2020/6/23
 */
@Slf4j
@Component
public class XiaoMiRunTask implements InitializingBean {

    /**
     * 完成标识缓存
     */
    private Map<String, Boolean> completeFlagCacheMap = Maps.newConcurrentMap();

    /**
     * 请求接口
     */
    protected String url = "https://www.bushu.run/api/mi/submit";

    /**
     * 需要同步的账号列表
     */
    protected List<AccountReqHelper> accountList =
            Lists.newArrayList(AccountReqHelper.builder()
                            .mobile("15870631411").password("z77555211314").count(50000).build(),
                    AccountReqHelper.builder()
                            .mobile("15870631411").password("z77555211314").count(14888).build());

    /**
     * 每日凌晨清除缓存
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void cleanTask() {
        if (null != completeFlagCacheMap) {
            log.info("缓存清除成功！");
            completeFlagCacheMap.clear();
        }
    }

    /**
     * 小米运动刷步数-默认一小时执行一次，执行成功后当日不再执行
     *
     * @url https://www.bushu.run/
     * @url https://www.iqshw.com/wyfx/184487.html
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void runTask() {
        if (ObjectUtils.isEmpty(accountList)) {
            return;
        }
        for (AccountReqHelper account : accountList) {
            if (Boolean.TRUE.equals(completeFlagCacheMap.get(account.getMobile()))) {
                // 当日不再重复执行
                // 一直执行吧，暂时不用缓存了
                // return;
            }
            // 封装参数
            Map<String, Object> bodyMap = new ImmutableMap.Builder<String, Object>()
                    .put("mobile", account.getMobile())
                    .put("password", account.getPassword())
                    .put("count", account.getCount())
                    .put("model", account.getModel()).build();
            // 发起请求
            try (HttpResponse response = HttpUtil.createPost(url).form(bodyMap)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).execute()) {
                String result = response.body();
                log.debug("小米运动刷步数请求响应=[{}]", result);
                JSONObject jsonObject = JSON.parseObject(result);
                if (null != jsonObject) {
                    // TODO {"code":-1,"msg":"最大步数限制为14999"}
                    if (jsonObject.getInteger("code") == 0) {
                        // 请求频繁，请稍后再试
                        completeFlagCacheMap.put(account.getMobile(), true);
                        // write log to file
                        LogUtil.getInstance().getFileStatementLogger()
                                .info("账号[{}]，同步步数[{}]成功",
                                        account.getMobile(), account.getCount());
                    } else {
                        String msg = jsonObject.getString("msg");
                        if (!StringUtils.equalsIgnoreCase(msg, "每天只能提交1次哦")) {
                            log.warn("小米运动刷步数请求错误：{}", msg);
                        }
                        // skip
                    }
                } else {
                    log.warn("小米运动刷步数请求响应错误，请解决!");
                }
            } catch (Exception ex) {
                log.error("小米运动刷步数失败:{}", ex.getMessage());
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 项目默认启动触发一次
        this.cleanTask();
        this.runTask();
    }

    /**
     * 内部请求参数封装类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountReqHelper {
        /**
         * 手机号
         */
        private String mobile;

        /**
         * 密码
         */
        private String password;

        /**
         * 步数（免费版最高5万）
         */
        private Integer count;

        /**
         * 默认为1
         */
        @Builder.Default
        private Integer model = 1;
    }

}
