package com.online.messageboard.mapper;

import com.online.messageboard.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 留言数据访问层接口
 * 定义留言表的所有数据库操作方法
 */
@Mapper
public interface MessageMapper {

    /**
     * 根据ID查询单条留言（包含回复信息）
     */
    Message selectById(Integer id);

    /**
     * 分页查询留言列表
     * @param offset 起始位置
     * @param limit 每页条数
     */
    List<Message> selectByPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据状态分页查询留言
     * @param status 状态：0=未处理，1=已回复
     * @param offset 起始位置
     * @param limit 每页条数
     */
    List<Message> selectByStatusAndPage(@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询总记录数
     */
    int countAll();

    /**
     * 根据状态查询记录数
     */
    int countByStatus(@Param("status") Integer status);

    /**
     * 插入新留言
     */
    int insert(Message message);

    /**
     * 根据ID更新留言状态
     */
    int updateStatusById(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 根据ID更新留言分类和违规标记
     */
    int updateTypeAndRiskById(@Param("id") Integer id, @Param("type") String type, @Param("isRisk") Integer isRisk);

    /**
     * 根据ID删除留言（级联删除回复由数据库外键约束处理）
     */
    int deleteById(Integer id);

    /**
     * 批量删除留言
     */
    int deleteBatchByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询各类型留言数量统计
     */
    List<Map<String, Object>> countByType();

    /**
     * 查询违规留言数量
     */
    int countRisk();

    /**
     * 查询所有留言（用于批量操作）
     */
    List<Message> selectAll();
}
