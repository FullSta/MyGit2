package cn.itcast.cxf.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import cn.itcast.cxf.domain.Car;
import cn.itcast.cxf.domain.User;

@WebService
public interface IUserService {
	@WebMethod
	public String sayHello(String name);

	@WebMethod
	public List<Car> findCarsByUser(User user);
}
