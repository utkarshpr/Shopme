 package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class userRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entitymanager;
	
//	@Test
//	public void testCreateNewUserwithOneRole() {
//		Role roleAdmin=entitymanager.find(Role.class, 1);
//		
//		User nadmin =new User("na22@codejava.net","name2020","Nam","Ha Minh");
//		nadmin.addRole(roleAdmin);
//		User saveUser=repo.save(nadmin);
//		assertThat(saveUser.getId()).isGreaterThan(0);
//	}
//	
	@Test
	public void testCreateNewUserwithTneRole() {
		
//		
//		User nadmin =new User("ravi@codejava.net","ravi2020","Ravi","Kumar");
//		Role roleEditor=new Role(3);
//		Role assistant=new Role(5);
//		
//		nadmin.addRole(roleEditor);
//		nadmin.addRole(assistant);
//		
//		User saveUser=repo.save(nadmin);
//		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		
		Iterable<User> listUser=repo.findAll();
		listUser.forEach(user->System.out.println(user));
		
		
	}
	@Test
	public void findByID()
	{
		User username=repo.findById(1).get();
		System.out.println(username);
		assertThat(username).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		
		User username=repo.findById(1).get();
//		username.setEnabled(true);
//		username.setEmail("utkarshbgp98@gmail.com");
//		username.setFirstname("Utkarsh");
//		username.setLastname("Pravind");
//		
		username.setPassword("utkarshpassword");
		repo.save(username);
		assertThat(username.getId()).isGreaterThan(0);
	}
	
	@Test
	public void updateUserRoles() {
		User username=repo.findById(5).get();
		Role role=new Role(1);
		Role roleq=new Role(2);
		username.getRoles().remove(role);
		username.addRole(roleq);
		repo.save(username);		
		
	}
	@Test
	public void deleteUser() {
		Integer userId=2;
		Integer userId1=5;
		
		repo.deleteById(userId);
		repo.deleteById(userId1);
	}
	@Test
	public void testGetUser() {
		String email ="utkarshbgp98@gmail.com";
	User user=	repo.getUserByEmail(email);
	assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		
		Integer id=1;
		Long count=repo.countById(id);
		assertThat(count).isNotNull().isGreaterThan(0);
	}
	@Test
	public void testDisable() {
		Integer id=1;
		repo.updateEnable(id, false);
		
	}
	@Test
	public void testEnable() {
		Integer id=1;
		repo.updateEnable(id, true);
		
	}
	@Test
	public void testListFirst() {
		int pageNumber=0;
		int pageSize=4;
		Pageable pagle=PageRequest.of(pageNumber, pageSize);
		Page<User> page=repo.findAll(pagle);
		List<User> listUser=page.getContent();
		listUser.forEach(user->System.out.println(user.toString()));
		assertThat(listUser.size()).isEqualTo(pageSize);
		
	}
	@Test
	public void testSearch() {
		int pageNumber=0;
		int pageSize=4;
		Pageable pagle=PageRequest.of(pageNumber, pageSize);
		Page<User> page=repo.findAll("bruce",pagle); 
		List<User> listUser=page.getContent();
		listUser.forEach(user->System.out.println(user.toString()));
		assertThat(listUser.size()).isGreaterThan(0);
		
	}
}
