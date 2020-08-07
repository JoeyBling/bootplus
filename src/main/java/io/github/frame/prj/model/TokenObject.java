package io.github.frame.prj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户请求Token
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Data
@Accessors(chain = true)
public class TokenObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户所属机构
     */
    private String appid;

}