package com.gamusdev.servletreactive.performance.data;

import com.gamusdev.servletreactive.performance.meter.data.DataManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayDeque;
import java.util.Map;

@ExtendWith(SpringExtension.class)
public class DataManagerTest {

    @InjectMocks
    private DataManager dataManager;

    @Test
    public void insertDurationTest(){
        dataManager.insertPostDuration(1l);
        dataManager.insertPutDuration(1l);
        dataManager.insertGetDuration(1l);
        dataManager.insertGetAllDuration(1l);
        dataManager.insertDeleteDuration(1l);

        dataManager.printPostInfo();
        dataManager.printPutInfo();
        dataManager.printGetInfo();
        dataManager.printGetAllInfo();
        dataManager.printDeleteInfo();
        dataManager.printMeanInfo();
        // Because of simplicity, use ReflectionTestUtils.
        // PowerMock could also be used
        var metrics = (Map<HttpMethod, ArrayDeque<Long>> ) ReflectionTestUtils.getField(dataManager, "metrics");
        Assertions.assertEquals(1l, metrics.get(HttpMethod.POST).getFirst());
        Assertions.assertEquals(1l, metrics.get(HttpMethod.PUT).getFirst());
        Assertions.assertEquals(1l, metrics.get(HttpMethod.GET).getFirst());
        Assertions.assertEquals(1l, metrics.get(HttpMethod.DELETE).getFirst());
    }

}
