package com.westlake.air.propro.dao;

import com.westlake.air.propro.domain.db.LibraryDO;
import com.westlake.air.propro.domain.query.LibraryQuery;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by James Lu MiaoShan
 * Time: 2018-06-13 13:16
 */
@Service
public class LibraryDAO extends BaseDAO<LibraryDO, LibraryQuery>{

    public static String CollectionName = "library";

    @Override
    protected String getCollectionName() {
        return CollectionName;
    }

    @Override
    protected Class getDomainClass() {
        return LibraryDO.class;
    }

    @Override
    protected boolean allowSort() {
        return true;
    }

    @Override
    protected Query buildQueryWithoutPage(LibraryQuery libraryQuery) {
        Query query = new Query();
        if (libraryQuery.getId() != null) {
            query.addCriteria(where("id").is(libraryQuery.getId()));
        }
        if (libraryQuery.getName() != null) {
            query.addCriteria(where("name").regex(libraryQuery.getName()));
        }
        if (libraryQuery.getType() != null) {
            query.addCriteria(where("type").is(libraryQuery.getType()));
        }
        return query;
    }

    public List<LibraryDO> getSimpleAll(Integer type) {
        Document queryDoc = new Document();
        if(type != null){
            queryDoc.put("type",type);
        }

        Document fieldsDoc = new Document();
        fieldsDoc.put("id",true);
        fieldsDoc.put("name",true);

        Query query = new BasicQuery(queryDoc, fieldsDoc);
        return mongoTemplate.find(query, LibraryDO.class, CollectionName);
    }

    public LibraryDO getByName(String name) {
        LibraryQuery query = new LibraryQuery();
        query.setName(name);
        return mongoTemplate.findOne(buildQuery(query), LibraryDO.class, CollectionName);
    }

}