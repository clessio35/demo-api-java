package demo.project.api.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import demo.project.api.service.DummyService;

public class DummyApiTests {

    private static ExtentReports extent;
    private static ExtentTest test;

    DummyService dum = new DummyService();

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            test.fail("Teste falhou: " + e.getMessage());
        }

        @Override
        protected void succeeded(Description description) {
            test.pass("Teste passou");
        }
    };

    @BeforeClass
    public static void beforeClass() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @org.junit.Before
    public void setup() {
        dum.accessApi();
        test = extent.createTest(testName.getMethodName());
    }

    @Test
    public void requestGETMethodForEndpointTEST() {
        test.info("Iniciando teste GET /test");
        dum.sendGetRequest("/test");
        dum.validateResponseForRequestGet();
        test.pass("GET /test validado com sucesso");
    }

    @Test
    public void requestGETMethodForEndpointUSERS() {
        test.info("Iniciando teste GET /users");
        dum.sendGetRequest("/users");
        dum.validateResponseForRequestGetUsers();
        test.pass("GET /users validado com sucesso");
    }

    @Test
    public void requestPOSTAuthToken() {
        test.info("Iniciando teste POST /auth/login");
        dum.sendPostRequestForAuthToken("/auth/login");
        dum.validateResponseForPostRequest();
        test.pass("POST /auth/login validado com sucesso");
    }

    @Test
    public void requestGetAuthProducts() {
        test.info("Iniciando teste GET /auth/products com token");
        dum.sendGetRequestWithAuth("/auth/products");
        dum.validateResponseForProducts();
        test.pass("GET /auth/products validado com sucesso");
    }

    @Test
    public void requestGetWithouAuthProducts() {
        test.info("Iniciando teste GET /auth/products sem token");
        dum.sendGetRequestWithoutAuth("/auth/products");
        dum.validateResponseForProductsWithouToken();
        test.pass("GET /auth/products sem token validado com sucesso");
    }

    @Test
    public void requestPostForCreatingProducts() {
        test.info("Iniciando teste POST /products/add");
        dum.sendPostRequestForCreatingProducts("/products/add");
        dum.validateResponseForProductCreated();
        test.pass("POST /products/add validado com sucesso");
    }

    @Test
    public void requestGetForProductsCreated() {
        test.info("Iniciando teste GET /products");
        dum.sendGetRequest("/products");
        dum.validateResponseForProducts();
        test.pass("GET /products validado com sucesso");
    }

    @Test
    public void requestGetForSpecificProductWithID() {
        test.info("Iniciando teste GET /products/{id}");
        dum.sendGetRequestWithID("/products");
        dum.validateResponseForProductsWithId();
        test.pass("GET /products/{id} validado com sucesso");
    }

    @Test
    public void requestGetInvalidId() {
        test.info("Iniciando teste GET /products/0 (id inválido)");
        dum.sendGetRequest("/products/0");
        dum.validateResponseWithInvalidId();
        test.pass("GET /products/0 validado (erro esperado)");
    }

    @AfterClass
    public static void afterClass() {
        extent.flush();
    }
}
