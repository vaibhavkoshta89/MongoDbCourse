package com.tengen;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by vaibhav on 07-01-2015.
 */
public class HelloWorldFromSpark {
    public static void main(String[] args) {
        Route route = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "Hello World from Spark";
            }
        };
        Spark.get("/",route);
    }
}
