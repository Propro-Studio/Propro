package com.westlake.air.pecs.domain.db;

import com.westlake.air.pecs.domain.BaseDO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-06-04 21:16
 */
@Data
@Document(collection = "library")
public class LibraryDO extends BaseDO {

    public static Integer TYPE_STANDARD = 0;
    public static Integer TYPE_VERIFY = 1;

    private static final long serialVersionUID = -3258829839160856625L;

    @Id
    String id;

    @Indexed(unique = true)
    String name;

    //0:标准库,1:校准库
    Integer type;

    String description;

    String instrument;

    Long proteinCount;

    Long peptideCount;

    Long totalCount;

    Long totalTargetCount;

    Long totalDecoyCount;

    Date createDate;

    Date lastModifiedDate;
}