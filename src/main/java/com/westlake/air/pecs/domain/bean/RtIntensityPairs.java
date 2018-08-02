package com.westlake.air.pecs.domain.bean;

import lombok.Data;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-07-22 22:40
 */
@Data
public class RtIntensityPairs {

    Float[] rtArray;

    Float[] intensityArray;

    public RtIntensityPairs(){}

    public RtIntensityPairs(Float[] rtArray, Float[] intensityArray){
        this.rtArray = rtArray;
        this.intensityArray = intensityArray;
    }
}
