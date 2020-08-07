package io.github.frame.prj.transfer.response;

import io.github.util.PageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页响应对象基类
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Data
@Accessors(chain = true)
public class TransPageBaseResponse extends TransBaseResponse {
    private static final long serialVersionUID = 1L;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 刷新分页信息
     *
     * @param page PageUtils
     */
    public TransPageBaseResponse flushPageList(PageUtils<?> page) {
        if (page != null) {
            this.page = new Page(page.getCurrPage(), page.getTotalPage(), page.getTotalCount(), page.getPageSize());
        }
        super.setList(page.getList());
        return this;
    }

    public Page getPage() {
        return this.page;
    }

    @Data
    @Accessors(chain = true)
    class Page {

        /**
         * 当前页数
         */
        private long pageNum;
        /**
         * 总页数
         */
        private long pages;
        /**
         * 总记录数
         */
        private long total;
        /**
         * 每页记录数
         */
        private long pageSize;

        public Page(long pageNum, long pages, long total, long pageSize) {
            this.pageNum = pageNum;
            this.pages = pages;
            this.total = total;
            this.pageSize = pageSize;
        }
    }

}
