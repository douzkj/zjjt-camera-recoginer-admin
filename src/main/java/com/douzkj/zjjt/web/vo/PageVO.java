package com.douzkj.zjjt.web.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ranger dong
 * @date 00:19 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final long DEFAULT_PAGE = 1L;
    private static final long DEFAULT_PAGE_SIZE = 20L;
    private static final long DEFAULT_PAGE_TOTAL = 0L;

    private Long page;

    private Long pageSize;

    private Long total;

    private Long totalPage;

    private List<T> items;

    public static <T> PageVO<T> of(Page<T> pageData) {
        return of(pageData, Function.identity());
    }

    public static <T, R> PageVO<T> of(Page<R> pageData, Function<R, T> fn) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPage(pageData.getCurrent());
        pageVO.setPageSize(pageData.getSize());
        pageVO.setTotal(pageData.getTotal());
        pageVO.setTotalPage(pageData.getPages());
        if (pageData.getRecords() != null ) {
            pageVO.setItems(pageData.getRecords().stream().map(fn).collect(Collectors.toList()));
        } else {
            pageVO.setItems(Collections.emptyList());
        }
        return pageVO;
    }


}
