package dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

public class Dataprovider {
    @DataProvider(name = "dataprovider")
    public Object[][] dp() {
        TestData testData = prepareTestData();
        int count = testData.getTestData().size();
        Object[][] objects = new Object[count][];
        for (int i = 0; i < count; i++) {
            objects[i] = new Object[] {testData.getTestData().get(i)};
        }
        return objects;
    }

    private TestData prepareTestData(){
        TestData testData;
        try {
            testData = new ObjectMapper().readValue(getClass().getClassLoader().getResource("json/testData.json"), TestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return testData;
    }

}
