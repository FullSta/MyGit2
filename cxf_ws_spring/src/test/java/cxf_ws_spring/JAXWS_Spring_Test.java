package cxf_ws_spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.cxf.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class JAXWS_Spring_Test {
	@Autowired
	private IUserService proxy;

	@Test
	public void testCXF() {
		System.out.println(proxy.sayHello("zahnsagn"));
	}
}
