package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IMenuExtDao extends IMenuDao{
    /**
     * 根据父ID，查询孩子节点
     *
     * @param parentId
     * @return
     */
    List<MenuDO> queryListParentId(Long parentId);

    /**
     * 查询没有button的节点
     *
     * @return
     */
    List<MenuDO> queryNotButtonList();

    /**
     * 查询所有菜单
     *
     * @return
     */
    List<MenuDO> queryAllMenus();

    /**
     * 查询所有菜单ID
     *
     * @return
     */
    List<Long> queryAllButtonMenuId();
}
