package cn.itcast.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	// 注入DAO
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> findNoAssociationCustomers() {
		// fixedAreaId is null
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(
			String fixedAreaId) {
		// fixedAreaId is ?
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void associationCustomersToFixedArea(String customerIdStr,
			String fixedAreaId) {
		// 解除关联动作
		customerRepository.clearFixedAreaId(fixedAreaId);

		if (customerIdStr.equals("null")){
			return;
		}
		String[] customerIdArray = customerIdStr.split(",");
		for (String idStr : customerIdArray) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId, id);
		}
	}

	@Override
	public void regist(Customer customer) {
		System.out.println("传到service层的customer"+customer);
		customerRepository.save(customer);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

	@Override
	public Customer login(String telephone, String password) {
		System.out.println("这里是crm_management的serviceImpl   telephone:"+telephone+"  password:"+password);
		return customerRepository.findByTelephoneAndPassword(telephone,password);
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		System.out.println(address);
		String fixedAreaId =customerRepository.findFixedAreaIdByAddress(address);
        System.out.println("接收到的数据:"+fixedAreaId);
		return fixedAreaId;
	}

}
