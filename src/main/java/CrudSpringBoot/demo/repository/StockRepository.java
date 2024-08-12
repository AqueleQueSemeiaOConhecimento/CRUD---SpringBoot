package CrudSpringBoot.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import CrudSpringBoot.demo.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, String>{

}
