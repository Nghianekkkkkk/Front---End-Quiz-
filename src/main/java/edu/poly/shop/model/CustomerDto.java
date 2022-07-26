package edu.poly.shop.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
	private Long  customerId ;
	
	@NotEmpty
	@NotBlank(message = "Email không được để trống")
	private String email;
	
	@NotEmpty
	@NotBlank(message = "Tên tài khoản không được để trống")
	private String name;
	
	@NotEmpty
	@NotBlank(message = "Mật khẩu không được để trống")
	private String password;
	
	@NotEmpty
	@NotBlank(message = "Số điện thoại không được để trống")
	private String phone;
	
	private Date registeredDate;
	
	@NotEmpty
	@NotBlank(message = "Không được để trống mô tả")
	private String status;
	
	private Boolean isEdit = false;
}
