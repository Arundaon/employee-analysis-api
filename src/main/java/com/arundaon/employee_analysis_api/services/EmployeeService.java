package com.arundaon.employee_analysis_api.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.arundaon.employee_analysis_api.entities.Employee;
import com.arundaon.employee_analysis_api.models.*;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class EmployeeService {


    private ElasticsearchClient esClient;

    public EmployeeService( ElasticsearchClient esClient) {

        this.esClient = esClient;
    }

    public Long getCount() throws IOException {

        CountRequest countRequest = CountRequest.of(c -> c
                .index("companydatabase")
                .query(QueryBuilders.matchAll().build()._toQuery())
        );

        CountResponse countResponse = esClient.count(countRequest);

        return countResponse.count();

    }

    public Double getAverageSalary() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations("average_salary", AggregationBuilders.avg().field("Salary").build()._toAggregation())
        );

        SearchResponse<Employee> searchResponse = esClient.search(searchRequest, Employee.class);
        Aggregate aggregation = searchResponse.aggregations().get("average_salary");

        return aggregation.avg().value();
    }
    public MinMaxResponse getMinMaxSalary() throws IOException {
        HashMap<String,Aggregation> aggregations = new HashMap<>();
        aggregations.put("min_salary",AggregationBuilders.min().field("Salary").build()._toAggregation() );
        aggregations.put("max_salary", AggregationBuilders.max().field("Salary").build()._toAggregation());

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations(aggregations)
        );

        SearchResponse<Employee> searchResponse = esClient.search(searchRequest, Employee.class);

        double minSalary = searchResponse.aggregations().get("min_salary").min().value();
        double maxSalary = searchResponse.aggregations().get("max_salary").max().value();

        return new MinMaxResponse(minSalary, maxSalary);
    }

    public List<AgeBucket> getAgeDistributionHistogram() throws IOException {
        SearchRequest request = SearchRequest.of(s-> s
                .index("companydatabase")
                .size(0)
                        .query(QueryBuilders.matchAll().build()._toQuery())
                        .aggregations("age_distribution",a -> a
                                .histogram( h -> h
                                        .field("Age")
                                        .interval(5.0)
                                )
                        )
                );


        SearchResponse<Void> response = esClient.search(request,Void.class);
        return response.aggregations().get("age_distribution").histogram().buckets().array().stream().map( bucket -> AgeBucket
                .builder().age(bucket.key())
                .count(bucket.docCount()).build()
        ).toList();
    }

    public List<MaritalBucket> getMaritalStatusDistribution() throws IOException {
        SearchRequest request = SearchRequest.of(s-> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations("marital_status_distribution",AggregationBuilders.terms().field("MaritalStatus.raw").build()._toAggregation())
        );


        SearchResponse<Void> response = esClient.search(request,Void.class);
        return response.aggregations().get("marital_status_distribution").sterms().buckets().array().stream().map( bucket -> MaritalBucket
                .builder().marital_status(bucket.key().stringValue())
                .count(bucket.docCount()).build()
        ).toList();
    }

    public List<DateBucket> getDateOfJoiningHistogram() throws IOException {
        SearchRequest request = SearchRequest.of(s-> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations("date_of_joining_distribution",a -> a
                        .dateHistogram( h -> h
                                .field("DateOfJoining")
                                .calendarInterval(CalendarInterval.Year)
                                .format("yyyy")
                        )
                )
        );


        SearchResponse<Void> response = esClient.search(request,Void.class);
        return response.aggregations().get("date_of_joining_distribution").dateHistogram().buckets().array().stream().map( bucket -> DateBucket
                .builder().year(bucket.keyAsString())
                .count(bucket.docCount()).build()
        ).toList();
    }

    public List<InterestBucket> getTopInterestsDistribution() throws IOException {
        SearchRequest request = SearchRequest.of(s-> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations("interests_distribution",AggregationBuilders.terms().field("Interests.raw").size(10).build()._toAggregation())
        );


        SearchResponse<Void> response = esClient.search(request,Void.class);
        return response.aggregations().get("interests_distribution").sterms().buckets().array().stream().map( bucket -> InterestBucket
                .builder().interest(bucket.key().stringValue())
                .count(bucket.docCount()).build()
        ).toList();
    }

    public List<DesignationBucket> getDesignationDistribution() throws IOException {
        SearchRequest request = SearchRequest.of(s-> s
                .index("companydatabase")
                .size(0)
                .query(QueryBuilders.matchAll().build()._toQuery())
                .aggregations("designation_distribution",AggregationBuilders.terms().field("Designation.raw").size(100).build()._toAggregation())
        );


        SearchResponse<Void> response = esClient.search(request,Void.class);
        return response.aggregations().get("designation_distribution").sterms().buckets().array().stream().map( bucket -> DesignationBucket
                .builder().designation(bucket.key().stringValue())
                .count(bucket.docCount()).build()
        ).toList();
    }

}
