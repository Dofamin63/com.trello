package core;

import core.models.RequestModel;
import core.models.ResponseModel;
import io.restassured.http.ContentType;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiManager {


    public ResponseModel create(String endPoint, RequestModel request) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().log().all()
                .post(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }

    public ResponseModel create(String endPoint, RequestModel request,  Map<String, String> params) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .pathParams(params)
                .when().log().all()
                .post(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }

    public ResponseModel addAttachment(String endPoint, String path) {
        File file = new File(path);
        return given()
                .contentType("multipart/form-data")
                .multiPart(file).log().all()
                .post(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }

    public ResponseModel update(String endPoint, RequestModel request) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().log().all()
                .put(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }
    public ResponseModel update(String endPoint, RequestModel request,  Map<String, String> params) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .pathParams(params)
                .when().log().all()
                .put(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }

    public ResponseModel delete(String endPoint) {
        return given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .delete(endPoint)
                .then().log().all().extract().as(ResponseModel.class);
    }
}
