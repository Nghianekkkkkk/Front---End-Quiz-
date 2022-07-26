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
@Table(name = "orders")
public class Order implements Serializable{
	@Id//khoa chinh
	@GeneratedValue(strategy = GenerationType.IDENTITY)//truong tu tang
	private int orderId;
	@Temporal(TemporalType.DATE)//kieu du lieu date
	private Date orderDate;
//	@Column(nullable = false)
//	private int customerId;
	@Column(nullable = false)
	private double amount;
	@Column(nullable = false)
	private short status;
	
	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails;
}
