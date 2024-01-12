package tech.wedev.wecom.standard.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.entity.ExcelDatum;
import tech.wedev.wecom.entity.po.OpLogPO;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.WecomMarketArticleQO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.enums.OpTypeEnum;
import tech.wedev.wecom.mapper.OpLogMapper;
import tech.wedev.wecom.mapper.WecomMarketArticleMapper;
import tech.wedev.wecom.standard.ClientMsgReadLogService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.util.*;

/**
 * 资讯阅读日志记录服务实现
 */
@Service
public class ClientMsgReadLogServiceImpl implements ClientMsgReadLogService {

    @Resource
    private WecomRequestService wecomRequestService;

    @Resource
    private OpLogMapper opLogMapper;

    @Resource
    WecomMarketArticleMapper wecomMarketArticleMapper;

    @Resource
    private CacheManager caffeineCacheManager;

    @Override
    public ResponseVO saveLog(String articleSource, String code) {
        ResponseVO<Object> responseVO = ResponseVO.error();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(articleSource)) {
            responseVO.setRetMsg("参数为空");
            return responseVO;
        }

        String key = "wecom";
        //尝试从缓存中获取
        Cache caffeineCache = caffeineCacheManager.getCache("wecom-cache");
        Object value = Objects.requireNonNull(caffeineCache, "通过CacheName：wecom-cache拿不到Cache对象，请检查").get(key, WecomMarketArticlePO.class);
        if (value == null) {
            //缓存未命中，从数据库加载
            value = wecomMarketArticleMapper.select(WecomMarketArticleQO.builder().articleSource(articleSource).build());
            caffeineCache.put(key, value);
        }

        WecomMarketArticlePO wecomMarketArticlePO = wecomMarketArticleMapper.selectOneByArticleSource(articleSource);
        if (wecomMarketArticlePO == null) {
            responseVO.setRetMsg("该资讯不存在");
            return responseVO;
        }

        if (wecomMarketArticlePO.getIsDeleted() == 1) {
            responseVO.setRetMsg("该资讯已被发布者删除");
            return responseVO;
        }
        if (wecomMarketArticlePO.getStatus() == 3) {
            responseVO.setRetMsg("该资讯已作废");
            return responseVO;
        }
        if (StringUtils.isEmpty(wecomMarketArticlePO.getArticleLink())) {
            responseVO.setRetMsg("跳转失败，链接为空，请检查！");
            return responseVO;
        }
        Map<String, Object> authResult = wecomRequestService.authGetUserInfo(code);
        if ((Integer) authResult.get("errcode") != 0) {
            responseVO.setRetMsg("调用企微API返回错误");
            return responseVO;
        }
        String userId = MapUtils.getString(authResult, "userid");
        Map<String, String> content = new HashMap<>();
        content.put("article_source", articleSource);
        JSONObject log = new JSONObject();
        log.put("data", content);
        OpLogPO opLogPO = OpLogPO.builder()
                .opUserId(userId)
                .opTellerno("测试统一认证号")
                .isDeleted(0)
                .createId(0L)
                .gmtCreate(new Date())
                .gmtModified(new Date())
                .modifiedId(0L)
                .opType(OpTypeEnum.READ_MSG.getCode())
                .corpId("测试租户")
                .orgCode("测试机构")
                .opContent(JSON.toJSONString(log)).build();
        opLogMapper.saveLog(opLogPO);
        responseVO = ResponseVO.success();
        responseVO.setRetMsg(wecomMarketArticlePO.getArticleLink());
        return responseVO;
    }

    @Override
    public void export(HttpServletRequest request, OutputStream out, Map<String, Object> param) {
        ExcelWriter excelWriter = EasyExcelFactory.write(out).build();

        //头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //单元格策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setWrapped(Boolean.TRUE);
        //设置垂直居中为居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        //初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        WriteSheet ws = EasyExcelFactory.writerSheet(0).head(ExcelDatum.class).registerWriteHandler(horizontalCellStyleStrategy).build();
        List<ExcelDatum> data = Lists.newArrayList();
        param.keySet().forEach(a -> data.add(ExcelDatum.builder().sapNo(a).result(MapUtils.getString(param, a)).build()));
        excelWriter.write(data, ws);
        excelWriter.finish();
    }
}
