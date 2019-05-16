package com.westlake.air.propro.service;

import com.westlake.air.propro.domain.ResultDO;
import com.westlake.air.propro.domain.bean.analyse.ComparisonResult;
import com.westlake.air.propro.domain.db.AnalyseOverviewDO;
import com.westlake.air.propro.domain.query.AnalyseOverviewQuery;

import java.util.HashSet;
import java.util.List;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-07-19 16:38
 */
public interface AnalyseOverviewService {

    List<AnalyseOverviewDO> getAllByExpId(String expId);

    Long count(AnalyseOverviewQuery query);

    ResultDO<List<AnalyseOverviewDO>> getList(AnalyseOverviewQuery targetQuery);

    ResultDO insert(AnalyseOverviewDO overviewDO);

    ResultDO update(AnalyseOverviewDO overviewDO);

    ResultDO delete(String id);

    ResultDO deleteAll(String id);

    ResultDO deleteAllByExpId(String expId);

    ResultDO<AnalyseOverviewDO> getById(String id);

    ResultDO<AnalyseOverviewDO> getFirstByExpId(String expId);

    ComparisonResult comparison(HashSet<String> overviewIds);
}