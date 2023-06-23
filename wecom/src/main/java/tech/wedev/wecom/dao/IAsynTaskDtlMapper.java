package tech.wedev.wecom.dao;

import org.apache.ibatis.annotations.*;
import tech.wedev.autm.asyntask.AsynTaskBean;
import tech.wedev.autm.asyntask.entity.AsynTaskDtl;

import java.util.List;

@Mapper
public interface IAsynTaskDtlMapper {

    @Delete({" delete from sys_asyn_task_dtl where TASK_ID=#{taskId}"})
    int deleteByPrimary(String taskId);

    @Select({"<script>" +
            "       select task_id,\n" +
            "       keywords, task_type,\n" +
            "       sub_task_type,\n" +
            "       state,\n" +
            "       priority,\n" +
            "       apply_time,\n" +
            "       in_queue_time,\n" +
            "       ip,\n" +
            "       thread_id,\n" +
            "       start_work_time,\n" +
            "       end_work_time,\n" +
            "       err_code,\n" +
            "       err_msg,\n" +
            "       deal_num,\n" +
            "       timeout_limit,\n" +
            "       plan_time,\n" +
            "       ref_col1,\n" +
            "       ref_col2,\n" +
            "       ref_col3,\n" +
            "       info1,\n" +
            "       info2,\n" +
            "       info3" +
            "from sys_asyn_task_dtl\n" +
            "where TASK_ID=#{taskId}" +
            "</script>"})
    @Results({
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "keywords", property = "keywords"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "state", property = "state"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "in_queue_time", property = "inQueueTime"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "thread_id", property = "threadId"),
            @Result(column = "start_work_time", property = "startWorkTime"),
            @Result(column = "end_work_time", property = "endWorkTime"),
            @Result(column = "err_code", property = "errCode"),
            @Result(column = "err_msg", property = "errMsg"),
            @Result(column = "deal_num", property = "dealNum"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "plan_time", property = "planTime"),
            @Result(column = "ref_col1", property = "refCol1"),
            @Result(column = "ref_col2", property = "refCol2"),
            @Result(column = "ref_col3", property = "refCol3"),
            @Result(column = "info1", property = "info1"),
            @Result(column = "info2", property = "info2"),
            @Result(column = "info3", property = "info3")
    })
    AsynTaskDtl selectByPrimary(String taskId);

    @Insert({"<script>" +
            "       insert into sys_asyn_task_dtl (" +
            "       task_id,\n" +
            "       keywords, task_type,\n" +
            "       sub_task_type,\n" +
            "       state,\n" +
            "       priority,\n" +
            "       apply_time,\n" +
            "       in_queue_time,\n" +
            "       ip,\n" +
            "       thread_id,\n" +
            "       start_work_time,\n" +
            "       end_work_time,\n" +
            "       err_code,\n" +
            "       err_msg,\n" +
            "       deal_num,\n" +
            "       timeout_limit,\n" +
            "       plan_time,\n" +
            "       ref_col1,\n" +
            "       ref_col2,\n" +
            "       ref_col3,\n" +
            "       info1,\n" +
            "       info2,\n" +
            "       info3" +
            ")\n" +
            " values \n" +
            "       <foreach collection='asynTaskDtlList' separator=',' item='item'>" +
            "           (#{item.taskId}, #{item.keywords}, #{item.taskType}, #{item.subTaskType}, #{item.state}, #{item.priority},\n" +
            "           #{item.applyTime}, #{item.inQueueTime}, #{item.ip}, #{item.threadId}, #{item.startWorkTime}, #{item.endWorkTime},\n" +
            "           #{item.errCode}, #{item.errMsg}, #{item.dealNum}, #{item.timeoutLimit}, #{item.planTime}, #{item.refCol1},\n" +
            "           #{item.refCol2}, #{item.refCol3}, #{item.info1}, #{item.info2}, #{item.info3})\n" +
            "       </foreach>" +
            "</script>"})
    int batchInsert(List<AsynTaskDtl> asynTaskDtlList);

    @Insert({"<script>" +
            "       insert into sys_asyn_task_dtl (" +
            "       task_id,\n" +
            "       keywords, task_type,\n" +
            "       sub_task_type,\n" +
            "       state,\n" +
            "       priority,\n" +
            "       apply_time,\n" +
            "       in_queue_time,\n" +
            "       ip,\n" +
            "       thread_id,\n" +
            "       start_work_time,\n" +
            "       end_work_time,\n" +
            "       err_code,\n" +
            "       err_msg,\n" +
            "       deal_num,\n" +
            "       timeout_limit,\n" +
            "       plan_time,\n" +
            "       ref_col1,\n" +
            "       ref_col2,\n" +
            "       ref_col3,\n" +
            "       info1,\n" +
            "       info2,\n" +
            "       info3" +
            ")\n" +
            " values (\n" +
            "           (#{asynTaskDtl.taskId}, #{asynTaskDtl.keywords}, #{asynTaskDtl.taskType}, #{asynTaskDtl.subTaskType}, #{asynTaskDtl.state}, #{asynTaskDtl.priority},\n" +
            "           #{asynTaskDtl.applyTime}, #{asynTaskDtl.inQueueTime}, #{asynTaskDtl.ip}, #{asynTaskDtl.threadId}, #{asynTaskDtl.startWorkTime}, #{asynTaskDtl.endWorkTime},\n" +
            "           #{asynTaskDtl.errCode}, #{asynTaskDtl.errMsg}, #{asynTaskDtl.dealNum}, #{asynTaskDtl.timeoutLimit}, #{asynTaskDtl.planTime}, #{asynTaskDtl.refCol1},\n" +
            "           #{asynTaskDtl.refCol2}, #{asynTaskDtl.refCol3}, #{asynTaskDtl.info1}, #{asynTaskDtl.info2}, #{asynTaskDtl.info3})\n" +
            "</script>"})
    int insert(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    /**
     * 根据索引更新对应任务的状态为进入队列状态
     *
     * @param oldState
     * @param newState
     * @param ip
     * @param taskType
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state= #{newState}, in_queue_time = now()\n" +
            "       where task_type = #{taskType}\n" +
            "       and state = #{oldState}\n" +
            "       and ip = #{ip}" +
            "</script>"})
    int updateInQueueStateByIndex(@Param("oldState") String oldState,
                                  @Param("newState") String newState,
                                  @Param("ip") String ip,
                                  @Param("taskType") String taskType);

    /**
     * 根据主键更新信息（适合一般的更新）
     * @param asynTaskDtl
     * @return
     */
    @Update({"<script>" +
            " update sys_asyn_task_dtl\n" +
            "       <trim prefix=\"set\" suffix=\" \" suffixOverrides=\",\">\n" +
            "           <if test=\"asynTaskDtl.state != null and asynTaskDtl.state != ''\">\n" +
            "               state = #{asynTaskDtl.state},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.errCode != null and asynTaskDtl.errCode != ''\">\n" +
            "               err_code = #{asynTaskDtl.errCode},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.errMsg != null and asynTaskDtl.errMsg != ''\">\n" +
            "               err_msg = #{asynTaskDtl.errMsg},\n" +
            "           </if>\n" +
            "       </trim>\n" +
            "       where task_id = #{asynTaskDtl.taskId}\n" +
            "</script>"})
    int updateByPrimary(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    /**
     * 更新状态为待处理（'0'）任务的抢占IP（特殊更新）
     * @param ip
     * @param taskType
     * @param fetchTaskCount
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set ip= #{ip}\n" +
            "       where task_type = #{taskType}\n" +
            "       and state = '0'\n" +
            "       and ip is null\n" +
            "       limit ${fetchTaskCount}" +
            "</script>"})
    int updateIpByIndex(@Param("ip") String ip,
                        @Param("taskType") String taskType,
                        @Param("fetchTaskCount") int fetchTaskCount);

    /**
     * 修改关联字段及备注字段
     * @param asynTaskDtl
     * @return
     */
    @Update({"<script>" +
            " update sys_asyn_task_dtl\n" +
            "       <trim prefix=\"set\" suffix=\" \" suffixOverrides=\",\">\n" +
            "           <if test=\"asynTaskDtl.refCol1 != null and asynTaskDtl.refCol1 != ''\">\n" +
            "               ref_Col1 = #{asynTaskDtl.refCol1},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.refCol2 != null and asynTaskDtl.refCol2 != ''\">\n" +
            "               ref_Col2 = #{asynTaskDtl.refCol2},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.refCol3 != null and asynTaskDtl.refCol3 != ''\">\n" +
            "               ref_Col3 = #{asynTaskDtl.refCol3},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.info1 != null and asynTaskDtl.info1 != ''\">\n" +
            "               info1 = #{asynTaskDtl.info1},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.info2 != null and asynTaskDtl.info2 != ''\">\n" +
            "               info2 = #{asynTaskDtl.info2},\n" +
            "           </if>\n" +
            "           <if test=\"asynTaskDtl.info3 != null and asynTaskDtl.info3 != ''\">\n" +
            "               info3 = #{asynTaskDtl.info3},\n" +
            "           </if>\n" +
            "       </trim>\n" +
            " where task_id = #{asynTaskDtl.taskId}\n" +
            "</script>"})
    int updateRefColAndInfoByIndex(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    /**
     * 根据IP和任务类型更新状态
     * @param ip
     * @param taskType
     * @param state
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state = #{state}\n" +
            "   where task_type = #{taskType}\n" +
            "       and state = #{oldState}\n" +
            "       and ip = #{ip}" +
            "</script>"})
    int updateStateByIpAndTaskType(@Param("ip") String ip,
                                   @Param("taskType") String taskType,
                                   @Param("state") String state);

    /**
     * 更新指定taskId任务状态
     * @param taskId
     * @param state
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state = #{state}\n" +
            "   where task_id = #{taskId}\n" +
            "</script>"})
    int updateStateByIpAndTaskId(@Param("taskId") String taskId, @Param("state") String state);

    /**
     * 根据索引更新对应任务的状态为开始处理状态（特殊更新）
     * @param oldState
     * @param newState
     * @param threadId
     * @param ip
     * @param taskType
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state = #{newState}, start_work_time = now(), thread_id = #{threadId}\n" +
            "   where task_type = #{taskType}\n" +
            "       and state = #{oldState}\n" +
            "       and ip = #{ip}" +
            "</script>"})
    int updateStartWorkStateByIndex(@Param("oldState") String oldState,
                                    @Param("newState") String newState,
                                    @Param("threadId") String threadId,
                                    @Param("ip") String ip,
                                    @Param("taskType") String taskType);

    /**
     * 根据索引更新对应任务的状态为处理成功/失败状态（特殊更新）
     * @param oldState
     * @param newState
     * @param taskBean
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state = #{newState}, end_work_time = now(), thread_id = #{taskBean.threadId}, err_code = #{taskBean.errCode}, err_msg = #{taskBean.errMsg}\n" +
            "   where task_id = #{taskBean.taskId}\n" +
            "       and state = #{oldState}\n" +
            "</script>"})
    int updateSuccessOrFailStateByIndex(@Param("oldState") String oldState,
                                         @Param("newState") String newState,
                                         @Param("taskBean") AsynTaskBean taskBean);

    /**
     * 根据主键把任务状态置为撤销（特殊更新）
     * @param asynTaskDtl
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set state = #{asynTaskDtl.state}, err_code = #{asynTaskDtl.errCode}, err_msg = #{asynTaskDtl.errMsg}\n" +
            "   where task_id = #{asynTaskDtl.taskId}\n" +
            "       and state = '0'\n" +
            "</script>"})
    int updateRevokeStateByPrimary(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    @Select({"<script>" +
            "       select task_id,\n" +
            "           keywords, task_type,\n" +
            "           sub_task_type,\n" +
            "           state,\n" +
            "           priority,\n" +
            "           apply_time,\n" +
            "           in_queue_time,\n" +
            "           ip,\n" +
            "           thread_id,\n" +
            "           start_work_time,\n" +
            "           end_work_time,\n" +
            "           err_code,\n" +
            "           err_msg,\n" +
            "           deal_num,\n" +
            "           timeout_limit,\n" +
            "           plan_time,\n" +
            "           ref_col1,\n" +
            "           ref_col2,\n" +
            "           ref_col3,\n" +
            "           info1,\n" +
            "           info2,\n" +
            "           info3" +
            "       from sys_asyn_task_dtl\n" +
            "       <if test=\"asynTaskDtl != null\">\n" +
            "           where 1=1\n" +
            "               <if test=\"asynTaskDtl.taskType != null and asynTaskDtl.taskType != ''\">\n" +
            "                   and task_type = #{asynTaskDtl.taskType}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.state != null and asynTaskDtl.state != ''\">\n" +
            "                   and state = #{asynTaskDtl.state}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.ip != null and asynTaskDtl.ip != ''\">\n" +
            "                   and ip = #{asynTaskDtl.ip}\n" +
            "               </if>\n" +
            "       </if>\n" +
            "</script>"})
    @Results({
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "keywords", property = "keywords"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "state", property = "state"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "in_queue_time", property = "inQueueTime"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "thread_id", property = "threadId"),
            @Result(column = "start_work_time", property = "startWorkTime"),
            @Result(column = "end_work_time", property = "endWorkTime"),
            @Result(column = "err_code", property = "errCode"),
            @Result(column = "err_msg", property = "errMsg"),
            @Result(column = "deal_num", property = "dealNum"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "plan_time", property = "planTime"),
            @Result(column = "ref_col1", property = "refCol1"),
            @Result(column = "ref_col2", property = "refCol2"),
            @Result(column = "ref_col3", property = "refCol3"),
            @Result(column = "info1", property = "info1"),
            @Result(column = "info2", property = "info2"),
            @Result(column = "info3", property = "info3")
    })
    List<AsynTaskDtl> selectAll(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    @Select({"<script>" +
            "       select task_id,\n" +
            "           keywords, task_type,\n" +
            "           sub_task_type,\n" +
            "           state,\n" +
            "           priority,\n" +
            "           apply_time,\n" +
            "           in_queue_time,\n" +
            "           ip,\n" +
            "           thread_id,\n" +
            "           start_work_time,\n" +
            "           end_work_time,\n" +
            "           err_code,\n" +
            "           err_msg,\n" +
            "           deal_num,\n" +
            "           timeout_limit,\n" +
            "           plan_time,\n" +
            "           ref_col1,\n" +
            "           ref_col2,\n" +
            "           ref_col3,\n" +
            "           info1,\n" +
            "           info2,\n" +
            "           info3" +
            "       from sys_asyn_task_dtl\n" +
            "       <if test=\"asynTaskDtl != null\">\n" +
            "           where 1=1\n" +
            "               <if test=\"asynTaskDtl.keywords != null and asynTaskDtl.keywords != ''\">\n" +
            "                   and keywords = #{asynTaskDtl.keywords}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.taskType != null and asynTaskDtl.taskType != ''\">\n" +
            "                   and task_type = #{asynTaskDtl.taskType}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.subTaskType != null and asynTaskDtl.subTaskType != ''\">\n" +
            "                   and sub_task_type = #{asynTaskDtl.subTaskType}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.state != null and asynTaskDtl.state != ''\">\n" +
            "                   and state = #{asynTaskDtl.state}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.applyTime != null and asynTaskDtl.applyTime != ''\">\n" +
            "                   and date_format(apply_time,'%Y%m%d%H%i%s') like #{asynTaskDtl.applyTime}\n" +
            "               </if>\n" +
            "               <if test=\"asynTaskDtl.ip != null and asynTaskDtl.ip != ''\">\n" +
            "                   and ip = #{asynTaskDtl.ip}\n" +
            "               </if>\n" +
            "       </if>\n" +
            "order by apply_time desc limit 50" +
            "</script>"})
    @Results({
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "keywords", property = "keywords"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "state", property = "state"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "in_queue_time", property = "inQueueTime"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "thread_id", property = "threadId"),
            @Result(column = "start_work_time", property = "startWorkTime"),
            @Result(column = "end_work_time", property = "endWorkTime"),
            @Result(column = "err_code", property = "errCode"),
            @Result(column = "err_msg", property = "errMsg"),
            @Result(column = "deal_num", property = "dealNum"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "plan_time", property = "planTime"),
            @Result(column = "ref_col1", property = "refCol1"),
            @Result(column = "ref_col2", property = "refCol2"),
            @Result(column = "ref_col3", property = "refCol3"),
            @Result(column = "info1", property = "info1"),
            @Result(column = "info2", property = "info2"),
            @Result(column = "info3", property = "info3")
    })
    List<AsynTaskDtl> selectAllLimit(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    /**
     * 需重做任务时更新
     * @param asynTaskDtl
     * @return
     */
    @Update({"<script>" +
            "   update sys_asyn_task_dtl\n" +
            "       set keywords = #{asynTaskDtl.keywords},\n" +
            "           task_type = #{asynTaskDtl.taskType},\n" +
            "           state = #{asynTaskDtl.state},\n" +
            "           priority = #{asynTaskDtl.priority},\n" +
            "           apply_time = #{asynTaskDtl.applyTime},\n" +
            "           in_queue_time = #{asynTaskDtl.inQueueTime},\n" +
            "           ip = #{asynTaskDtl.ip},\n" +
            "           thread_id = #{asynTaskDtl.threadId},\n" +
            "           start_work_time = #{asynTaskDtl.startWorkTime},\n" +
            "           end_work_time = #{asynTaskDtl.endWorkTime},\n" +
            "           err_code = #{asynTaskDtl.errCode},\n" +
            "           err_msg = #{asynTaskDtl.errMsg},\n" +
            "           deal_num = #{asynTaskDtl.dealNum},\n" +
            "           timeout_limit = #{asynTaskDtl.timeoutLimit},\n" +
            "           plan_time = #{asynTaskDtl.planTime},\n" +
            "           ref_col1 = #{asynTaskDtl.refCol1},\n" +
            "           ref_col2 = #{asynTaskDtl.refCol2},\n" +
            "           ref_col3 = #{asynTaskDtl.refCol3},\n" +
            "           info1 = #{asynTaskDtl.info1},\n" +
            "           info2 = #{asynTaskDtl.info2},\n" +
            "           info3 = #{asynTaskDtl.info3}\n" +
            "   where task_id = #{asynTaskDtl.taskId}" +
            "</script>"})
    int updateAsynTask(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    @Select("       select dtl.task_id,\n" +
            "           dtl.keywords, dtl.task_type,\n" +
            "           dtl.sub_task_type,\n" +
            "           dtl.state,\n" +
            "           dtl.priority,\n" +
            "           dtl.apply_time,\n" +
            "           dtl.in_queue_time,\n" +
            "           dtl.ip,\n" +
            "           dtl.thread_id,\n" +
            "           dtl.start_work_time,\n" +
            "           dtl.end_work_time,\n" +
            "           dtl.err_code,\n" +
            "           dtl.err_msg,\n" +
            "           dtl.deal_num,\n" +
            "           dtl.timeout_limit,\n" +
            "           dtl.plan_time,\n" +
            "           dtl.ref_col1,\n" +
            "           dtl.ref_col2,\n" +
            "           dtl.ref_col3,\n" +
            "           dtl.info1,\n" +
            "           dtl.info2,\n" +
            "           dtl.info3" +
            "       from sys_asyn_task_dtl dtl\n" +
            "       where dtl.state = '2' " +
            "       and dtl.task_type = #{asynTaskDtl.taskType} " +
            "       and TIMESTAMPDIF(second, dtl.start_work_time, now()) > #{asynTaskDtl.timeoutLimit}")
    @Results({
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "keywords", property = "keywords"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "state", property = "state"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "in_queue_time", property = "inQueueTime"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "thread_id", property = "threadId"),
            @Result(column = "start_work_time", property = "startWorkTime"),
            @Result(column = "end_work_time", property = "endWorkTime"),
            @Result(column = "err_code", property = "errCode"),
            @Result(column = "err_msg", property = "errMsg"),
            @Result(column = "deal_num", property = "dealNum"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "plan_time", property = "planTime"),
            @Result(column = "ref_col1", property = "refCol1"),
            @Result(column = "ref_col2", property = "refCol2"),
            @Result(column = "ref_col3", property = "refCol3"),
            @Result(column = "info1", property = "info1"),
            @Result(column = "info2", property = "info2"),
            @Result(column = "info3", property = "info3")
    })
    List<AsynTaskDtl> queryWorkingTimeOutAsynTask(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);

    @Select("       select dtl.task_id,\n" +
            "           dtl.keywords, dtl.task_type,\n" +
            "           dtl.sub_task_type,\n" +
            "           dtl.state,\n" +
            "           dtl.priority,\n" +
            "           dtl.apply_time,\n" +
            "           dtl.in_queue_time,\n" +
            "           dtl.ip,\n" +
            "           dtl.thread_id,\n" +
            "           dtl.start_work_time,\n" +
            "           dtl.end_work_time,\n" +
            "           dtl.err_code,\n" +
            "           dtl.err_msg,\n" +
            "           dtl.deal_num,\n" +
            "           dtl.timeout_limit,\n" +
            "           dtl.plan_time,\n" +
            "           dtl.ref_col1,\n" +
            "           dtl.ref_col2,\n" +
            "           dtl.ref_col3,\n" +
            "           dtl.info1,\n" +
            "           dtl.info2,\n" +
            "           dtl.info3" +
            "       from sys_asyn_task_dtl dtl\n" +
            "       where dtl.state = '1' " +
            "       and dtl.task_type = #{asynTaskDtl.taskType} " +
            "       and TIMESTAMPDIF(second, dtl.in_queue_time, now()) > #{asynTaskDtl.timeoutLimit}*4")
    @Results({
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "keywords", property = "keywords"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "sub_task_type", property = "subTaskType"),
            @Result(column = "state", property = "state"),
            @Result(column = "priority", property = "priority"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "in_queue_time", property = "inQueueTime"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "thread_id", property = "threadId"),
            @Result(column = "start_work_time", property = "startWorkTime"),
            @Result(column = "end_work_time", property = "endWorkTime"),
            @Result(column = "err_code", property = "errCode"),
            @Result(column = "err_msg", property = "errMsg"),
            @Result(column = "deal_num", property = "dealNum"),
            @Result(column = "timeout_limit", property = "timeoutLimit"),
            @Result(column = "plan_time", property = "planTime"),
            @Result(column = "ref_col1", property = "refCol1"),
            @Result(column = "ref_col2", property = "refCol2"),
            @Result(column = "ref_col3", property = "refCol3"),
            @Result(column = "info1", property = "info1"),
            @Result(column = "info2", property = "info2"),
            @Result(column = "info3", property = "info3")
    })
    List<AsynTaskDtl> queryWaitingTimeOutAsynTask(@Param("asynTaskDtl") AsynTaskDtl asynTaskDtl);
}
