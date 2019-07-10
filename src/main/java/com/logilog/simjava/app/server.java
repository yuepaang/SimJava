package com.logilog.simjava.app;

import static spark.Spark.*;

public class server {
    public static void main(String[] args) {
        exception(Exception.class, ((e, request, response) -> e.printStackTrace()));
        staticFiles.location("/public");
        port(5000);
        get("/", ((request, response) -> "Hello, world"));
    }
}
