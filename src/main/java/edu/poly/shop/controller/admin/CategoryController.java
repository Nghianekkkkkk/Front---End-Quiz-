package edu.poly.shop.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Category;
import edu.poly.shop.model.CategoryDto;
import edu.poly.shop.service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {//lop catc 
	@Autowired
	CategoryService categoryService;//khai bao lop service
	
	@GetMapping("add")//khai bao anntion requestmapping
	public String add(Model model) {//them moi 1 admin
		model.addAttribute("category", new CategoryDto());//
		return"admin/categories/addOrEdit";//tra ve file html
	}
	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap model,@PathVariable("categoryId")Long categoryId) {//chinh sua tt entity
		
		Optional<Category> opt = categoryService.findById(categoryId);//import cts goi thuc hien pt findbyid
		CategoryDto dto = new CategoryDto();//tao ra dt categorydao
		
		if(opt.isPresent()) {//kt neu co gt return
			Category entity = opt.get();//lay tt cua cate 
			
			BeanUtils.copyProperties(entity, dto);//su dung copyProperties tu entity sang dto
			dto.setIsEdit(true);//neu dang che do edit se mang gt true
			
			model.addAttribute("category",dto);//tl gt tt category
			
			return new ModelAndView("admin/categories/addOrEdit",model);//sau khi co tt csdl tra ve view
		}
		
		model.addAttribute("message","Category is not existed");
		
		return new ModelAndView("forward:/admin/categories",model);
	}
	
	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap model, @PathVariable("categoryId")Long categoryId) {//xoa 1 category
		
		categoryService.deleteById(categoryId);//xoa theo id
		
		model.addAttribute("message","Category is deleted!");//hien thi thong bao 
		
		return new ModelAndView("forward:/admin/categories/search",model);
	}
	
	@PostMapping("saveOrUpdate")//kich hoat action pt post
	public ModelAndView saveOrUpdate(ModelMap model, //khai bao model, ctgdao
			@Valid @ModelAttribute("category") CategoryDto dto,BindingResult result) {//action luu nd ctr
		
		if(result.hasErrors()) {
			
			return new ModelAndView("admin/categories/addOrEdit");
		}
		
		Category entity = new Category();//tao ra entity lop domain
		BeanUtils.copyProperties(dto, entity);//su dung copyProperties tu dto sang entiy
		
		categoryService.save(entity);//goi thuc hien pt luu 
		
		model.addAttribute("message","Category is saved!");//dua ra tb da luu entity
		
		return new ModelAndView("forward:/admin/categories",model);//action chuyen huong den list
	}
	
	@RequestMapping("")
	public String list(ModelMap model) {//ht ds ctg
		List<Category> list= categoryService.findAll();//goi thuc hien pt findall tra ve ds 
		
		model.addAttribute("categories",list);
		
		return "admin/categories/list";
	}
	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name="name",required = false) String name) {//search tt theo name
		List<Category> list = null;
		
		if(StringUtils.hasText(name)) {//neu name co gt 
			list = categoryService.findByNameContaining(name);//goi pt find by name
		}else {
			list = categoryService.findAll();//return tra ve tat ca category
		}
		
		model.addAttribute("categories",list);//tl ds cho tt category
		
		return "admin/categories/search";//hien thi view search
	}
	
	@GetMapping("searchpaginated")
	public String search(ModelMap model, 
			@RequestParam(name="name", required = false) String name,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		
		int currentPage = page.orElse(1);//khai bao bien,k nhap gt sd orElse
		int pageSize = size.orElse(5);
		
		Pageable pageable = PageRequest.of(currentPage-1, pageSize,Sort.by("name"));
		Page<Category> resultPage = null;//dinh nghia category
		
		if(StringUtils.hasText(name)) {//neu name dc tt
			resultPage = categoryService.findByNameContaining(name,pageable);//truyen tham so pageable
			model.addAttribute("name",name);//hien thi return view
		}else {
			resultPage = categoryService.findAll(pageable);
		}
		
		int totalPages = resultPage.getTotalPages();//tra ve tong so cac trang 
		if(totalPages > 0) {
			int start = Math.max(1, currentPage-2);
			int end = Math.min(currentPage + 2, totalPages);
			
			if(totalPages > 5) {
				if(end == totalPages) start = end - 5;
				else if (start == 1) end = start + 5;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
					.boxed()
					.collect(Collectors.toList());
			
			model.addAttribute("pageNumbers",pageNumbers);
		}
		
		model.addAttribute("categoryPage",resultPage);
		
		return "admin/categories/searchpaginated";
	}
}
