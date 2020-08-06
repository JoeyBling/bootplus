package io.github.frame.prj.transfer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 响应对象基类
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class TransBaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    @Builder.Default
    private String code = "-1";

    /**
     * 对外显示的错误信息
     */
    private String msg;

    /**
     * 警告代码
     */
    private String warnCode;

    /**
     * 警告信息
     */
    private String warnMsg;

    /**
     * 客户端传入服务器的值，在请求响应中会返回这个值到客户端
     */
    private String clientStr;

    /**
     * 任意对象
     */
    private Object obj;

    /**
     * 列表对象
     */
    private List<?> list;

    /**
     * 是否请求成功
     *
     * @return boolean
     */
    public boolean isSucc() {
        return StringUtils.trimToEmpty(code).equals("0");
    }

}
