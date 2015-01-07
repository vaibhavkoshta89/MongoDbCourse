package com.tengen;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhav on 07-01-2015.
 */
public class HelloWorldFromSparkFreemarker {
    public static void main(String[] args) throws UnknownHostException {
        final Configuration configuration = new Configuration();

        configuration.setClassForTemplateLoading(
                HelloWorldFreemarkerStyle.class, "/");
        MongoClient client =
                new MongoClient("localhost", 27017);

        DB database = client.getDB("course");
        final DBCollection collection = database.getCollection("course");
        Route route = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");

                    DBObject object = collection.findOne();

                    helloTemplate.process(object, writer);


                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };
        Spark.get("/", route);
    }
}
