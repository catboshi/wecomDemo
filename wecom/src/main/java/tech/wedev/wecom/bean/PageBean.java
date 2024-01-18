package tech.wedev.wecom.bean;

import tech.wedev.wecom.utils.BeanUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageBean<T> implements Serializable {
    /**
     * 显示多少个
     */
    private Integer pageSize = 10;
    /**
     * 当前页数
     */
    private Integer pageNum = 1;
    /**
     * 总页数
     */
    private Integer pageCount = 0;
    /**
     * 总条数
     */
    private Integer total = 0;

    private List<T> result;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getTotal() {
        return total;
    }

    public List<T> getResult() {
        return result;
    }

    public static <S, D> PageBean<D> copyProperty(PageBean<S> s, Class<D> d) {
        s.result = BeanUtil.defaultIfNull(s.result, new ArrayList<>());
        PageBean<D> pageBeanDest = BeanUtil.copyProperty(s, PageBean.class);
        pageBeanDest.result = BeanUtil.copyProperties(s.result, d);
        pageBeanDest.total = s.getTotal();
        pageBeanDest.pageCount = s.getPageCount();
        pageBeanDest.pageNum = s.getPageNum();
        pageBeanDest.pageSize = s.getPageSize();
        return pageBeanDest;
    }

    public static <T> PageBean build(Integer pageSize, Integer pageNum, List<T> result) {
        return build(pageSize, pageNum, 0, result);
    }

    public static <T> PageBean build(Integer pageSize, Integer pageNum, Integer total, List<T> result) {
        PageBean pageBean = new PageBean();
        pageBean.pageSize = pageSize;
        pageBean.pageNum = pageNum;
        pageBean.result = result;
        pageBean.total = total;
        pageBean.pageCount = 0;
        if (result!=null) {
            if (total>0) {
                if (total%pageBean.pageSize!=0) {
                    pageBean.pageCount = 1;
                }
                pageBean.pageCount += total / pageBean.pageSize;
            }
        }
        return pageBean;
    }
}
