package dev.sonnyjon.recipespringmongodb.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Sonny on 7/19/2022.
 */
@Configuration
@EnableMongoRepositories(basePackages = "dev.sonnyjon.recipespringmongodb.repositories")
public class MongoConfig extends AbstractMongoClientConfiguration
{
    @Override
    protected String getDatabaseName()
    {
        return "test";
    }

    @Override
    public MongoClient mongoClient()
    {
        // TODO: Have connection string and database name pull from properties file.
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/test");
        MongoClientSettings mongoClientSettings = MongoClientSettings
                                                        .builder()
                                                        .applyConnectionString(connectionString)
                                                        .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection getMappingBasePackages()
    {
        return Collections.singleton("dev.sonnyjon.recipespringmongodb");
    }
}