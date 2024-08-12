package CrudSpringBoot.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import CrudSpringBoot.demo.entity.Account;

public interface AccountRepository extends JpaRepository <Account, UUID>{

}
