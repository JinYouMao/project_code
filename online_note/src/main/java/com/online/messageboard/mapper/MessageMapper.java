package com.online.messageboard.mapper;

import com.online.messageboard.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 留言Mapper接口
 * 提供留言相关的数据库操作方法
 */
@Mapper
public interface MessageMapper {
    
    /**
     * 插入留言
     * @param message 留言对象
     * @return 影响行数
     */
    @Insert("INSERT INTO message (username, content, contact, create_time, status, type, is_risk) VALUES (#{username}, #{content}, #{contact}, #{createTime}, #{status}, #{type}, #{isRisk})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);
    
    /**
     * 根据ID删除留言
     * @param id 留言ID
     * @return 影响行数
     */
    @Delete("DELETE FROM message WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量删除留言
     * @param ids 留言ID列表
     * @return 影响行数
     */
    @Delete("<script>DELETE FROM message WHERE id IN <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    int deleteBatch(@Param("ids") List<Integer> ids);
    
    /**
     * 更新留言
     * @param message 留言对象
     * @return 影响行数
     */
    @Update("<script>UPDATE message <set> <if test='username != null'>username = #{username},</if> <if test='content != null'>content = #{content},</if> <if test='contact != null'>contact = #{contact},</if> <if test='status != null'>status = #{status},</if> <if test='type != null'>type = #{type},</if> <if test='isRisk != null'>is_risk = #{isRisk},</if> </set> WHERE id = #{id}</script>")
    int update(Message message);
    
    /**
     * 根据ID查询留言
     * @param id 留言ID
     * @return 留言对象
     */
    @Select("SELECT * FROM message WHERE id = #{id}")
    @Results({
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "isRisk", column = "is_risk")
    })
    Message selectById(@Param("id") Integer id);
    
    /**
     * 分页查询留言列表
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 留言列表
     */
    @Select("SELECT * FROM message ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    @Results({
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "isRisk", column = "is_risk")
    })
    List<Message> selectList(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 根据状态筛选留言
     * @param status 留言状态（0=未处理，1=已回复）
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 留言列表
     */
    @Select("SELECT * FROM message WHERE status = #{status} ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    @Results({
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "isRisk", column = "is_risk")
    })
    List<Message> selectByStatus(@Param("status") Integer status, 
                                  @Param("offset") Integer offset, 
                                  @Param("limit") Integer limit);
    
    /**
     * 查询留言总数
     * @return 留言总数
     */
    @Select("SELECT COUNT(*) FROM message")
    int selectCount();
    
    /**
     * 根据状态查询留言数量
     * @param status 留言状态
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM message WHERE status = #{status}")
    int selectCountByStatus(@Param("status") Integer status);
    
    /**
     * 获取统计数据
     * @return 统计数据Map
     */
    @Select("SELECT COUNT(*) as total, SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as unprocessed, SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as processed, SUM(CASE WHEN type = '咨询' THEN 1 ELSE 0 END) as consultation, SUM(CASE WHEN type = '建议' THEN 1 ELSE 0 END) as suggestion, SUM(CASE WHEN type = '投诉' THEN 1 ELSE 0 END) as complaint, SUM(CASE WHEN type = '其他' OR type = '未知' OR type IS NULL THEN 1 ELSE 0 END) as other, SUM(CASE WHEN is_risk = 1 THEN 1 ELSE 0 END) as risky FROM message")
    Map<String, Object> selectStatistics();
    
    /**
     * 更新留言状态
     * @param id 留言ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE message SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    /**
     * 更新留言AI分类
     * @param id 留言ID
     * @param type 分类类型
     * @return 影响行数
     */
    @Update("UPDATE message SET type = #{type} WHERE id = #{id}")
    int updateType(@Param("id") Integer id, @Param("type") String type);
    
    /**
     * 更新留言违规标记
     * @param id 留言ID
     * @param isRisk 是否违规
     * @return 影响行数
     */
    @Update("UPDATE message SET is_risk = #{isRisk} WHERE id = #{id}")
    int updateIsRisk(@Param("id") Integer id, @Param("isRisk") Integer isRisk);
    
    /**
     * 批量更新违规标记
     * @param ids 留言ID列表
     * @param isRisk 是否违规
     * @return 影响行数
     */
    @Update("<script>UPDATE message SET is_risk = #{isRisk} WHERE id IN <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    int batchUpdateIsRisk(@Param("ids") List<Integer> ids, @Param("isRisk") Integer isRisk);
}
