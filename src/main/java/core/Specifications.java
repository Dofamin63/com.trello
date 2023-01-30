package core;

import utils.DBUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public static RequestSpecification requestSpec(String url, String path, String userName) {
        return new RequestSpecBuilder()
                .setBaseUri(url).setBasePath(path)
                .addQueryParams(DBUtils.getUserData(userName))
                .addFilter(new AllureRestAssured()).build();
    }
    public static ResponseSpecification responseSpec(Integer status) {
        return new ResponseSpecBuilder().expectStatusCode(status).build();
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
