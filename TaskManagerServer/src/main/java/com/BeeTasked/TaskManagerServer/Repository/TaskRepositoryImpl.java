package com.BeeTasked.TaskManagerServer.Repository;

import com.BeeTasked.TaskManagerServer.collections.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Task> find(Map<String, Object> query) {
        List<AggregationOperation> operations = new ArrayList<>();

        // Match operation for filtering
        Criteria criteria = new Criteria();
        query.forEach((key, value) -> criteria.and(key).is(value));
        operations.add(Aggregation.match(criteria));

        // Lookup operation to join with team collection
        operations.add(Aggregation.lookup("teams", "team", "_id", "teamDetails"));

        // Unwind operation for teamDetails
        operations.add(Aggregation.unwind("teamDetails", true));

        // Project operation to include only necessary fields
        operations.add(Aggregation.project("stage", "isTrashed", "teamDetails.name", "teamDetails.role", "teamDetails.email"));

        // Sort operation
        operations.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "_id")));

        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Task> results = mongoTemplate.aggregate(aggregation, "tasks", Task.class);

        return results.getMappedResults();
    }

    @Override
    public Task findTaskById(String id) {
        List<AggregationOperation> operations = new ArrayList<>();

        // Match operation for filtering by id
        operations.add(Aggregation.match(Criteria.where("_id").is(id)));

        // Lookup operation to join with team collection
        operations.add(Aggregation.lookup("teams", "team", "_id", "teamDetails"));

        // Unwind operation for teamDetails
        operations.add(Aggregation.unwind("teamDetails", true));

        // Lookup operation to join with activities.by collection
        operations.add(Aggregation.lookup("users", "activities.by", "_id", "activityDetails"));

        // Unwind operation for activityDetails
        operations.add(Aggregation.unwind("activityDetails", true));

        // Project operation to include only necessary fields
        operations.add(Aggregation.project("stage", "isTrashed", "teamDetails.name", "teamDetails.role", "teamDetails.email", "activityDetails.name"));

        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Task> results = mongoTemplate.aggregate(aggregation, "tasks", Task.class);

        return results.getUniqueMappedResult();
    }

    @Override
    public void updateAllTrashedTasks(boolean isTrashed) {
        Query query = new Query(Criteria.where("isTrashed").is(true));
        Update update = new Update().set("isTrashed", isTrashed);
        mongoTemplate.updateMulti(query, update, Task.class);
    }
}
