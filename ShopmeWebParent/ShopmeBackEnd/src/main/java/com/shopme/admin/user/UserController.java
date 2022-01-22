package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	public static final int User_Per_PAGE=4;
	@Autowired
	private UserService service;

	@GetMapping("/user")
	public String listAll(Model model) {
		return ListByPage(1,model,null);
	}
	@GetMapping("/user/page/{pageNum}")
	public String ListByPage(@PathVariable(name="pageNum") int pageNum,Model model,
			@Param("keyword") String keyword)  {
		System.out.println(keyword);
	Page <User> page=	service.listByPage(pageNum,keyword);
	List<User> List=page.getContent();
	
	long start=(pageNum-1)*UserService.User_Per_PAGE+1;
	long end=start+UserService.User_Per_PAGE-1;
	if(end>page.getTotalElements())
		end=page.getTotalElements();
	/*Filter:&nbsp;
		<input type="search" name="keyword"  class="form-control" />
		&nbsp;&nbsp;
		*/
	
	model.addAttribute("current", pageNum);
	model.addAttribute("start", start);
	model.addAttribute("end", end);
	model.addAttribute("listUsers", List);
	
	model.addAttribute("totalItems", page.getTotalElements());
	model.addAttribute("endPage",page.getTotalPages());
	model.addAttribute("keyword", keyword);
	return "user";
	}
	@GetMapping("/new")
	public String newUserAdd(Model model) {
		List<Role> listRole = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("listRole", listRole);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New User");
		return "User_form";
	}
	

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,@RequestParam("file") MultipartFile mf) throws IOException {
		System.out.println("===============================================================" + user);
		System.out.println(mf.getOriginalFilename());
		
		if(!mf.isEmpty())
		{
			String fileName=StringUtils.cleanPath(mf.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser=service.save(user);
			String uploadDir="user-photos/"+saveUser.getId();
			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mf);
			
		}
		else
		{
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		//
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully ...");
		return "redirect:/user";
	}
	

	@GetMapping("/edit{id}")
	public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {

		try {
			User user = service.get(id);
			model.addAttribute("user", user);
			List<Role> listRole = service.listRoles();
			model.addAttribute("listRole", listRole);

			model.addAttribute("pageTitle", "Edit User : " + id);
			return "user_form";
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/user";
		}
		
	} 
	@GetMapping("/delete{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {

		try {
			
			 service.deleteUserById(id);
			 redirectAttributes.addFlashAttribute("message","This user Id "+id+" deleted succesfully");
			 return "redirect:/user";			
		} catch (UserNotFoundException e) {
			
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/user";
		}
	}
	@GetMapping("/false{id}")
	public String disableUser(@PathVariable(name = "id") Integer id,RedirectAttributes redirectAttributes, Model model)
	{
		System.out.println(id);
		service.updateUserEnableStatus(id, false);
		return "redirect:/user";
	}
	@GetMapping("/true{id}")
	public String enableUser(@PathVariable(name = "id") Integer id,RedirectAttributes redirectAttributes, Model model)
	{
		System.out.println(id);
		service.updateUserEnableStatus(id, true);
		return "redirect:/user";
	}
}
