package com.tengen;

import com.mongodb.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Created by vaibhav on 10-01-2015.
 */
public class LoginDemo {
    public static void main(String[] args) throws UnknownHostException {
        final Configuration configuration = new Configuration();

        configuration.setClassForTemplateLoading(
                HelloWorldFreemarkerStyle.class, "/");
        MongoClient client =
                new MongoClient("localhost", 27017);

        DB database = client.getDB("login");
        final DBCollection collection = database.getCollection("users");
        Route route = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("Login.html");
                    DBObject object = collection.findOne();

                    helloTemplate.process(object, writer);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };

        Route route1 = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("Welcome.html");
                    DBObject object = collection.findOne();

                    helloTemplate.process(object, writer);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };

        Route registerRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("Register.html");
                    DBObject object = collection.findOne();

                    helloTemplate.process(object, writer);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };

        Route registerNewUser = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {
                    BasicDBObject emailObj = new BasicDBObject();
                    emailObj.put("email", request.queryParams("InputEmail1"));

                    DBObject object = collection.findOne(emailObj);

                    if (object == null) {
                        BasicDBObject documentDetail = new BasicDBObject();

                        documentDetail.put("email", request.queryParams("InputEmail1"));
                        documentDetail.put("password", request.queryParams("InputPassword1"));


                        collection.insert(documentDetail);
                        response.redirect("/");
                    } else {
                        StringBuilder html = new StringBuilder();
                        html.append("<h3>User already exist!</h3>");
                        return html.toString();
                    }

                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };

        Route loginRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                StringWriter writer = new StringWriter();
                try {

                    BasicDBObject emailObj = new BasicDBObject();
                    emailObj.put("email", request.queryParams("exampleInputEmail1"));
                    emailObj.put("password", request.queryParams("exampleInputPassword1"));
                    DBObject object = collection.findOne(emailObj);
                    System.out.println(object);
                    if (object != null) {
                        Template helloTemplate = configuration.getTemplate("Welcome.html");


                        helloTemplate.process(object, writer);
                    } else {
                        StringBuilder html = new StringBuilder();
                        html.append("<h3>User not found!</h3>");
                        return html.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return writer;
            }

        };

        Spark.post("/", registerNewUser);
        Spark.get("/", route);
        Spark.get("/Register", registerRoute);
        Spark.post("/Welcome", loginRoute);


    }
}
