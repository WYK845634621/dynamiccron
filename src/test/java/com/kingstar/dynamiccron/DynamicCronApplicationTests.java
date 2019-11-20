package com.kingstar.dynamiccron;

import com.kingstar.dynamiccron.task.DynamicTaskManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicCronApplicationTests {

	@Test
	public void contextLoads() {
		try{
			DynamicTaskManager.createJob("ebff5f08d9b84546a60be69725157a7e", "82107512a7824489853723d9090eff83");
			Thread.sleep(1000*60);
			DynamicTaskManager.reCreateJob("ebff5f08d9b84546a60be69725157a7e", "82107512a7824489853723d9090eff83");
			Thread.sleep(1000*60);
			DynamicTaskManager.closeJob("ebff5f08d9b84546a60be69725157a7e");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
