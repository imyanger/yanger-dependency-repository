package com.yanger.starter.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import lombok.Data;
import lombok.ToString;

/**
 * 分页对象
 * @Author yanger
 * @Date 2021/1/29 11:41
 */
@Data
@ToString(callSuper = true)
@JsonTypeName(value = "generalPage")
public class Page<T> implements IPage<T> {

    private static final long serialVersionUID = -7759805099824817556L;
    /** 查询数据列表 */
    private List<T> records = Collections.emptyList();
    /** 总数 */
    private long total = 0;
    /** 每页显示条数, 默认 10 */
    private long size = 10;
    /** 当前页 */
    private long current = 1;
    /** 排序字段信息 */
    private List<OrderItem> orders = new ArrayList<>();
    /** 自动优化 COUNT SQL */
    private boolean optimizeCountSql = true;
    /** 是否进行 count 查询 */
    private boolean isSearchCount = true;
    /** 是否命中 count 缓存 */
    private boolean hitCount = false;
    /** Start time */
    private Date startTime;
    /** End time */
    private Date endTime;

    /**
     * Page
     */
    public Page() { }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public Page(long current, long size) {
        this(current, size, 0);
    }

    /**
     * Page plus
     *
     * @param current   current
     * @param size      size
     * @param startTime start time
     * @param endTime   end time
     */
    public Page(long current, long size, Date startTime, Date endTime) {
        this(current, size);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Page
     *
     * @param current current
     * @param size    size
     * @param total   total
     */
    public Page(long current, long size, long total) {
        this(current, size, total, true);
    }

    /**
     * Page
     *
     * @param current       current
     * @param size          size
     * @param isSearchCount is search count
     */
    public Page(long current, long size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    /**
     * Page
     *
     * @param current       current
     * @param size          size
     * @param total         total
     * @param isSearchCount is search count
     */
    public Page(long current, long size, long total, boolean isSearchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    /**
     * Gets records *
     *
     * @return the records
     */
    @Override
    public List<T> getRecords() {
        return this.records;
    }

    /**
     * Sets records *
     *
     * @param records records
     * @return the records
     */
    @Override
    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    /**
     * Gets total *
     *
     * @return the total
     */
    @Override
    public long getTotal() {
        return this.total;
    }

    /**
     * Sets total *
     *
     * @param total total
     * @return the total
     */
    @Override
    public Page<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    /**
     * Gets size *
     *
     * @return the size
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /**
     * Sets size *
     *
     * @param size size
     * @return the size
     */
    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return this;
    }

    /**
     * Gets current *
     *
     * @return the current
     */
    @Override
    public long getCurrent() {
        return this.current;
    }

    /**
     * Sets current *
     *
     * @param current current
     * @return the current
     */
    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    /**
     * 查找 order 中正序排序的字段数组
     *
     * @param filter 过滤器
     * @return 返回正序排列的字段数组 string [ ]
     */
    @NotNull
    private String[] mapOrderToArray(Predicate<OrderItem> filter) {
        List<String> columns = new ArrayList<>(this.orders.size());
        this.orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

    /**
     * 移除符合条件的条件
     *
     * @param filter 条件判断
     */
    private void removeOrder(Predicate<OrderItem> filter) {
        for (int i = this.orders.size() - 1; i >= 0; i--) {
            if (filter.test(this.orders.get(i))) {
                this.orders.remove(i);
            }
        }
    }

    /**
     * 添加新的排序条件, 构造条件可以使用工厂
     *
     * @param items 条件
     * @return 返回分页参数本身 page
     */
    public Page<T> addOrder(OrderItem... items) {
        this.orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加新的排序条件, 构造条件可以使用工厂
     *
     * @param items 条件
     * @return 返回分页参数本身 page
     */
    public Page<T> addOrder(List<OrderItem> items) {
        this.orders.addAll(items);
        return this;
    }

    /**
     * Orders list
     *
     * @return the list
     */
    @Override
    public List<OrderItem> orders() {
        return this.getOrders();
    }

    /**
     * Gets orders *
     *
     * @return the orders
     */
    public List<OrderItem> getOrders() {
        return this.orders;
    }

    /**
     * Sets orders *
     *
     * @param orders orders
     */
    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    /**
     * Optimize count sql boolean
     *
     * @return the boolean
     */
    @Override
    public boolean optimizeCountSql() {
        return this.optimizeCountSql;
    }

    /**
     * Is search count boolean
     *
     * @return the boolean
     */
    @Override
    public boolean isSearchCount() {
        if (this.total < 0) {
            return false;
        }
        return this.isSearchCount;
    }

    /**
     * Sets search count *
     *
     * @param isSearchCount is search count
     * @return the search count
     */
    public Page<T> setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
        return this;
    }

    /**
     * Sets optimize count sql *
     *
     * @param optimizeCountSql optimize count sql
     * @return the optimize count sql
     */
    public Page<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    /**
     * Hit count *
     *
     * @param hit hit
     */
    @Override
    public void hitCount(boolean hit) {
        this.hitCount = hit;
    }

    /**
     * Is hit count boolean
     *
     * @return the boolean
     */
    @Override
    public boolean isHitCount() {
        return this.hitCount;
    }

}
