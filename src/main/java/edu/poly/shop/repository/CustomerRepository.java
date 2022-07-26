package edu.poly.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
