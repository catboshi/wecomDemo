package tech.wedev.wecom.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import tech.wedev.autm.asyntask.entity.AsynTaskDtl;

@Mapper
public interface IAsynTaskDtlHisMapper {

    @Insert({"INSERT INTO sys_asyn_task_dtl_his (" +
            "       TASK_ID,\n" +
            "       KEYWORDS,\n" +
            "       TASK_TYPE,\n" +
            "       SUB_TASK_TYPE,\n" +
            "       STATE, PRIORITY,\n" +
            "       APPLY_TIME,\n" +
            "       IN_QUEUE_TIME,\n" +
            "       IP,\n" +
            "       THREAD_ID,\n" +
            "       START_WORK_TIME,\n" +
            "       END_WORK_TIME,\n" +
            "       ERR_CODE,\n" +
            "       ERR_MSG,\n" +
            "       DEAL_NUM,\n" +
            "       TIMEOUT_LIMIT,\n" +
            "       PLAN_TIME,\n" +
            "       REF_COL1,\n" +
            "       REF_COL2,\n" +
            "       REF_COL3,\n" +
            "       INFO1,\n" +
            "       INFO2,\n" +
            "       INFO3" +
            ") " +
            "values (\n" +
            "       #{taskId}, #{keywords}, #{taskType}, #{subTaskType}, #{state}, #{priority},\n" +
            "       #{applyTime}, #{inQueueTime}, #{ip}, #{threadId}, #{startWorkTime}, #{endWorkTime},\n" +
            "       #{errCode}, #{errMsg}, #{dealNum}, #{timeoutLimit}, #{planTime}, #{refCol1},\n" +
            "       #{refCol2}, #{refCol3}, #{info1}, #{info2}, #{info3})"})
    int insert(AsynTaskDtl asynTaskDtl);
}
