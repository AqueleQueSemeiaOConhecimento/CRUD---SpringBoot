package CrudSpringBoot.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import CrudSpringBoot.demo.entity.BillingAddress;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID>{

}
