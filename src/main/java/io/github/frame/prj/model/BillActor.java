package io.github.frame.prj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作人信息
 *
 * @author Created by 思伟 on 2020/8/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillActor {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作IP
     */
    private String operateIp;

    /**
     * 客户端
     */
    private String client;
}
