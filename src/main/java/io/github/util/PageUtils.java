package io.github.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 分页工具类
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageUtils<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 每页记录数
     */
    private long pageSize;

    /**
     * 当前页数
     */
    private long currPage;

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 列表数据
     */
    private List<T> list = Collections.emptyList();

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    @Builder
    public PageUtils(List<T> list, long totalCount, long pageSize, long currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
//        this.totalPage = (long) Math.ceil(totalCount / pageSize);
        this.totalPage = getPages();
    }

    long getPages() {
        if (getPageSize() == 0) {
            return 0L;
        }
        long pages = getTotalCount() / getPageSize();
        if (getTotalCount() % getPageSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 构建分页工具类
     *
     * @param page mybatisplus分页对象
     * @param <T>  T
     * @return PageUtils<T>
     */
    public static <T> PageUtils<T> buildPageUtil(Page<T> page) {
        return Optional.ofNullable(page).map(tPage -> PageUtils.<T>builder()
                .list(tPage.getRecords())
                .totalCount(tPage.getTotal())
                .pageSize(tPage.getSize())
                .currPage(tPage.getCurrent()).build()).orElse(null);
    }

}
