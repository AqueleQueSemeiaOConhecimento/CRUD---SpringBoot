package CrudSpringBoot.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import CrudSpringBoot.demo.controller.dto.AccountStockResponseDto;
import CrudSpringBoot.demo.controller.dto.AssociateAccountStockDto;
import CrudSpringBoot.demo.entity.AccountStock;
import CrudSpringBoot.demo.entity.AccountStockId;
import CrudSpringBoot.demo.repository.AccountRepository;
import CrudSpringBoot.demo.repository.AccountStockRepository;
import CrudSpringBoot.demo.repository.StockRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private AccountStockRepository accountStockRepository;

    public void associateStock(String accountId, AssociateAccountStockDto associateAccountStockDto) {
     
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(associateAccountStockDto.stockId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        // DTO -> ENTITY
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
            id,
            account,
            stock,
            associateAccountStockDto.quantity()
        );

        accountStockRepository.save(entity);
        
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return account.getAccountStock()
        .stream()
        .map(as -> new AccountStockResponseDto(as.getStock().getStockId(), as.getQuantity(), 0.0))
        .toList();
    }

}
