package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	public static final int User_Per_PAGE=4;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PasswordEncoder passwordencoder;
	
	public List<User> listAll(){
		return (List<User>) userRepo.findAll();
		
	}
	public Page<User> listByPage(int pageNumber,String keyword){
		Sort sort=Sort.by("firstname");
		String sortDir="asc";
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNumber-1, User_Per_PAGE,sort);
		if(keyword!=null) {
			return userRepo.findAll(keyword, pageable);
		}
		return  userRepo.findAll(pageable);
	}
	public List<Role> listRoles(){
		return (List<Role>) roleRepo.findAll();
	}
	/**
	 * 
	 * @param save user
	 */
	public User save(User user) {
		// TODO Auto-generated method stub
		boolean isupdatingUser=(user.getId()!=null);
		if(isupdatingUser)
		{
			User existinguser=userRepo.findById(user.getId()).get();
			System.out.println(existinguser.getPassword());
			if(user.getPassword().isEmpty())
			{
				user.setPassword(existinguser.getPassword());
			}else {
				encodePassword(user);
			}
			
		}
		else
		{
			encodePassword(user);
		}
		
		return userRepo.save(user);
		
	}
	/**
	 * 
	 * @param user
	 */
	private void encodePassword(User user)
	{
		String encodePassword=passwordencoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}
	public boolean isEmailUnique(String email ,Integer id)
	{
		User userEmail=userRepo.getUserByEmail(email);
		if(userEmail == null) return true;
		
		boolean isCreatingNew=(id == null);
		if(isCreatingNew) {
			if(userEmail != null) return false;
		}
		else {
			if(userEmail.getId() !=id) {
				return false;
			}
			
		}
		
		return true;
	}
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get(); 
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find the user "+id);
		}
		
	}
	/**
	 * 
	 * @param id
	 * @throws UserNotFoundException 
	 */
	
	public void deleteUserById(Integer id) throws UserNotFoundException {
		Long count=userRepo.countById(id);
		if(count==null|| count==0)
		{
			throw new UserNotFoundException("Could not find the user "+id);
		}
		userRepo.deleteById(id);
	}
	public void updateUserEnableStatus(Integer id,boolean enable)
	{
		userRepo.updateEnable(id, enable);
	}
}
