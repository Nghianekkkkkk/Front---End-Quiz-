package edu.poly.shop.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity//khai bao entity
@Table(name= "products")
public class Product implements Serializable{
	@Id//khoa chinh
	@GeneratedValue(strategy = GenerationType.IDENTITY)//truong tu tang
	private Long productId;
	@Column(columnDefinition = "nvarchar(100) not null")
	private String name;
	@Column(nullable = false)//gia tri null
	private int quantity;
	@Column(nullable = false)//gia tri null
	private double unitPrice;
	@Column(length = 200)
	private String image;
	@Column(columnDefinition = "nvarchar(500) not null")
	private String description;
	@Column(nullable = false)
	private double discount;
	@Temporal(TemporalType.DATE)//khai bao kieu du lieu ngay 
	private Date enteredDate;
	@Column(nullable = false)
	private short status;
	
	@ManyToOne
	@JoinColumn(name= "categoryId")//moc noi quan he category
	private Category category;//product lay du lieu tu category
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)//quan he 1 nhieu 
	private Set<OrderDetail> orderDetails;//lay tap hop orderdetail
}
