package com.westlake.air.pecs.domain.bean;

import lombok.Data;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-07-22 22:40
 */
@Data
public class IntensityRtLeftRtRightPairs {

    Float[] rtLeftArray;

    Float[] rtRightArray;

    Float[] intensityArray;

    public IntensityRtLeftRtRightPairs(){}

    public IntensityRtLeftRtRightPairs(Float[] rtLeftArray, Float[] rtRightArray, Float[] intensityArray){
        this.rtLeftArray = rtLeftArray;
        this.rtRightArray = rtRightArray;
        this.intensityArray = intensityArray;
    }
}