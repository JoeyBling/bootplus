package io.github.frame.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.frame.prj.model.BaseEntity;
import io.github.frame.prj.service.ISimpleService;
import io.github.util.PageUtils;
import io.github.util.R;
import io.github.util.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 * simple模式控制器（控制simple页面）
 * TODO: shiro权限注解还未实现继承关系
 *
 * @param <T> entity
 * @param <S> service
 * @param <C> this
 * @author Created by 思伟 on 2020/8/26
 */
public abstract class SimpleController<T extends BaseEntity, S extends ISimpleService<T>, C>
        extends AbstractController<C> {

    @Autowired
//    @Resource
    protected S simpleService;

    /**
     * 解决JSON序列化问题
     */
    @Deprecated
    private T entity;

    protected void setEntity(T entity) {
        this.entity = entity;
    }

    public SimpleController() {
        Class clazz = getClass();
        try {
            // 处理有继承的情况，有继承时，取父类（ParameterizedType = 泛型实例）
            while (!ParameterizedType.class.isAssignableFrom(clazz.getGenericSuperclass().getClass())
                    && !clazz.equals(Object.class)) {
                clazz = clazz.getSuperclass();
            }
            // 取出第1个参数的实际类型（实体类对象类型）
            Class<T> entityClass = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
            entity = entityClass.newInstance();
        } catch (Throwable e) {
            logger.error("{}注入entity失败，请手动重写`setEntity()`方法，e={}", clazz, e.getMessage());
        }
    }

    /**
     * 获取访问路径，注意此地址需符合shiro权限校验字符串规则
     *
     * @return String
     */
    public abstract String getPath();

    /**
     * 模块
     */
    protected String getFrameModule() {
        return StringUtils.replace(getPath(), "/", "");
    }

    /**
     * 获取模块名称
     *
     * @return String
     */
    protected abstract String getFrameModuleName();

    /**
     * 禁用或删除的名称
     *
     * @return String
     */
    protected String getDisableName() {
        return "删除";
    }

    /**
     * info页显示前操作，可以写列表加载，默认值选中等
     *
     * @param t     entity
     * @param model 视图模型
     */
    protected void bindSelectData(T t, Model model) {
        model.addAttribute("t_version", T_VERSION);
        model.addAttribute("record", t);
        model.addAttribute("path", getPath());
        model.addAttribute("frameModule", getFrameModule());
        model.addAttribute("frameModuleName", getFrameModuleName());
    }

    /**
     * 分页列表前操作，可以写列表加载，默认值选中等
     *
     * @see #bindSelectData
     */
    protected void bindListData(T t, Model model) {
        bindSelectData(t, model);
    }

    /**
     * 列表页面
     */
    @RequiresPermissions(value = {"list", "select"}, logical = Logical.OR)
    @RequestMapping("/showList")
    public String showList(T t, Model model) {
        bindListData(t, model);
        return String.format("%sList", getPath());
    }

    /**
     * 列表查询前操作
     */
    protected void beforeList(T t) {
    }

    /**
     * 列表
     */
    @RequiresPermissions(value = {"list", "select"}, logical = Logical.OR)
    @RequestMapping("/list")
    @ResponseBody
    public R list(Integer offset, Integer limit, String sort, String order, T t) {
        beforeList(t);
        // 此处做`bootstrap-table`的转换
        offset = (getOffset(offset) / getLimit(limit)) + 1;
        Page<T> page = simpleService.getPage(offset, getLimit(limit), sort, isOrderByAsc(order), t);
        return R.ok().put("page", PageUtils.buildPageUtil(page));
    }

    /**
     * 详情
     */
    @RequestMapping("/select/{id}")
    @ResponseBody
    @RequiresPermissions("select")
    public R info(@PathVariable("id") Long id) {
        T entity = simpleService.getById(id);
        return R.ok().put("record", entity);
    }

    /**
     * 修改状态
     */
    @RequestMapping("/updateEnable")
    @ResponseBody
    @RequiresPermissions("update")
    public R updateEnable(Long id, Boolean enable) {
        boolean updateEnable = simpleService.updateEnable(id, Boolean.TRUE.equals(enable));
        return updateEnable ? R.ok() : R.error("修改失败!");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("delete")
    public R delete(@RequestParam("ids") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] idArr = toArrays(jsonArray);
        simpleService.removeByIds(Arrays.asList(idArr));
        return R.ok();
    }

    /**
     * 获取一个空的实体对象,防止NPE
     * 未来可能会删除
     *
     * @return T
     */
    @Deprecated
    private T emptyEntity() {
        try {
            return (T) entity.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

}
