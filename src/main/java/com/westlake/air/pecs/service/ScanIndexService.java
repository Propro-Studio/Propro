package com.westlake.air.pecs.service;

import com.westlake.air.pecs.domain.ResultDO;
import com.westlake.air.pecs.domain.db.simple.SimpleScanIndex;
import com.westlake.air.pecs.domain.db.ScanIndexDO;
import com.westlake.air.pecs.domain.query.ScanIndexQuery;

import java.util.HashMap;
import java.util.List;


/**
 * Created by James Lu MiaoShan
 * Time: 2018-06-06 09:36
 */
public interface ScanIndexService {

    List<ScanIndexDO> getAllByExperimentId(String experimentId);

    Long count(ScanIndexQuery query);

    ResultDO<List<ScanIndexDO>> getList(ScanIndexQuery query);

    List<ScanIndexDO> getAll(ScanIndexQuery query);

    List<SimpleScanIndex> getSimpleAll(ScanIndexQuery query);

    List<SimpleScanIndex> getSimpleList(ScanIndexQuery query);

    ResultDO insert(ScanIndexDO scanIndexDO);

    ResultDO update(ScanIndexDO scanIndexDO);

    ResultDO insertAll(List<ScanIndexDO> scanIndexDOList, boolean isDeleteOld);

    ResultDO deleteAllByExperimentId(String experimentId);

    ResultDO deleteAllSwathIndexByExperimentId(String experimentId);

    ResultDO<ScanIndexDO> getById(String id);

    HashMap<Float, ScanIndexDO> getSwathIndexList(String expId);
}
