package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRow() {
		Role roleadmin=new Role("Admin" ,"manage Everything");
		Role saverole= repo.save(roleadmin);
		assertThat(saverole.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson =new Role("Salesperson","manage product price ,customers ,shipping ,orders and sales report");
		Role roleEditor=new Role("Editor" ,"manage categories , brands,products , articles and menus");
		Role roleShipper=new Role("Shipper","view products ,view orders and update order status");
		Role roleAssistant=new Role("Assistant","manage question and reviews");
		
		repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper, roleAssistant));
	}
}
