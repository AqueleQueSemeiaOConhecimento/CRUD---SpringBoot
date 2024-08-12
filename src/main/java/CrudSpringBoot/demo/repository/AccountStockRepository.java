package CrudSpringBoot.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import CrudSpringBoot.demo.entity.AccountStock;
import CrudSpringBoot.demo.entity.AccountStockId;



public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId>{

}
