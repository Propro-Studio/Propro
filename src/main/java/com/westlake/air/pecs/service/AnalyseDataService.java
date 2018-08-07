package com.westlake.air.pecs.service;

import com.westlake.air.pecs.domain.ResultDO;
import com.westlake.air.pecs.domain.db.simple.TransitionGroup;
import com.westlake.air.pecs.domain.db.AnalyseDataDO;
import com.westlake.air.pecs.domain.query.AnalyseDataQuery;

import java.util.List;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-07-19 16:02
 */
public interface AnalyseDataService {

    List<AnalyseDataDO> getAllByOverviewId(String overviewId);

    Long count(AnalyseDataQuery query);

    ResultDO<List<AnalyseDataDO>> getList(AnalyseDataQuery convQuery);

    ResultDO insert(AnalyseDataDO convData);

    ResultDO insertAll(List<AnalyseDataDO> convList, boolean isDeleteOld);

    ResultDO delete(String id);

    ResultDO deleteAllByOverviewId(String overviewId);

    ResultDO<AnalyseDataDO> getById(String id);

    /**
     * 获取MS1的卷积卷积信息
     * @param overviewId
     * @param peptideRef
     * @return
     */
    ResultDO<AnalyseDataDO> getMS1Data(String overviewId, String peptideRef);

    /**
     * 获取MS2的卷积数据
     * @param overviewId
     * @param peptideRef
     * @param cutInfo
     * @return
     */
    ResultDO<AnalyseDataDO> getMS2Data(String overviewId, String peptideRef, String cutInfo);

    ResultDO<List<TransitionGroup>> getTransitionGroup(AnalyseDataQuery query);
}