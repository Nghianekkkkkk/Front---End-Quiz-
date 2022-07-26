package edu.poly.shop.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.classic.pattern.DateConverter;
import edu.poly.shop.domain.Account;
import edu.poly.shop.domain.Customer;
import edu.poly.shop.model.AccountDto;
import edu.poly.shop.model.CustomerDto;
import edu.poly.shop.service.CustomerService;

@Controller
@RequestMapping("site/customers")
public class CustomerController {
	@Autowired
	CustomerService customerService;
	
	@GetMapping("add")
	public String add(Model model) {
		
		model.addAttribute("customer", new CustomerDto());
		
		return "site/customers/addOrEdit";
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, 
			@Valid @ModelAttribute("customer") CustomerDto dto,BindingResult result) {
		
		if(result.hasErrors()) {
			System.out.println();
			return new ModelAndView("site/customers/addOrEdit");
		}
		
		Customer entity = new Customer();
		BeanUtils.copyProperties(dto, entity);
		
		customerService.save(entity);
		
		model.addAttribute("message","Customer is saved!");
		
		return new ModelAndView("forward:/site/customers",model);
	
	}
	
	@RequestMapping("")
	public String list(ModelMap model) {
		List<Customer> list= customerService.findAll();
		
		model.addAttribute("customers",list);
		
		return "site/customers/list";
	}
	
	@GetMapping("edit/{customerId}")
	public ModelAndView edit(ModelMap model,@PathVariable("customerId")Integer customerId) {
		
		Optional<Customer> opt = customerService.findById(customerId);
		
		CustomerDto dto = new CustomerDto();
		
		if(opt.isPresent()) {
			Customer entity = opt.get();
			
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			
			model.addAttribute("customer",dto);
			
			return new ModelAndView("site/customers/addOrEdit",model);
		}
		
		model.addAttribute("message","Customer is not existed");
		
		return new ModelAndView("forward:/site/customers",model);
	}
}
