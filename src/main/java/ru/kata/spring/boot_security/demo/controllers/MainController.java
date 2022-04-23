package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

	private UserService userService;

	public MainController(@Autowired UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/user")
	public ModelAndView mainPage() {
		ModelAndView modelAndView = new ModelAndView("user");
		modelAndView.getModel().put("user", userService.getUsers());
		return modelAndView;
	}

	@GetMapping(value = "/admin")
	public ModelAndView adminMainPage() {
		ModelAndView modelAndView = new ModelAndView("admin/list");
		modelAndView.getModel().put("users", userService.getUsers());
		return modelAndView;
	}

	@GetMapping(value = "/admin/add")
	public ModelAndView adminAddPage() {
		ModelAndView modelAndView = new ModelAndView("admin/form");
		modelAndView.getModel().put("user", new User());
		modelAndView.getModel().put("action", "PUT");
		return modelAndView;
	}

	@GetMapping(value = "/admin/edit/{id}")
	public ModelAndView adminEditPage(@PathVariable int id) {
		ModelAndView modelAndView = new ModelAndView("admin/form");
		modelAndView.getModel().put("user", userService.getUser(id));
		modelAndView.getModel().put("action", "/admin/edit/" + id);
		return modelAndView;
	}

	@PutMapping(value = "/admin/add")
	public ModelAndView createUser(@ModelAttribute("user") User user) {
		userService.addUser(user);
		return new ModelAndView("redirect:/admin");
	}

	@PostMapping(value = "/admin/edit/{id}")
	public ModelAndView updateUser(@ModelAttribute("user") User user, @PathVariable int id) {
		user.setId(id);
		userService.updateUser(user);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping (value = "/admin/delete/{id}")
	public ModelAndView deleteUser(@PathVariable int id) {
		userService.deleteUser(id);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

}
