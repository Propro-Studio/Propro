package com.westlake.air.propro.domain.bean.aird;

public class Features {


    public static String original_width = "owid";  //原始文件中前体的窗口大小,未经过ExperimentDO.overlap参数调整,precursor mz
    public static String original_precursor_mz_start = "oMzStart";    //原始文件中前体的荷质比窗口开始位置,未经过ExperimentDO.overlap参数调整,precursor mz
    public static String original_precursor_mz_end = "oMzEnd";   //原始文件中前体的荷质比窗口结束位置,未经过ExperimentDO.overlap参数调整,precursor mz

    public static String raw_id = "rawId";   //从Vendor文件中得到的msd.id
    public static String ignore_zero_intensity = "ignoreZeroIntensity"; //是否忽略Intensity为0的键值对,默认为true
    public static String overlap = "overlap";  //SWATH的各个窗口间的重叠部分


    public static String slope = "slope";
    public static String intercept = "intercept";

}