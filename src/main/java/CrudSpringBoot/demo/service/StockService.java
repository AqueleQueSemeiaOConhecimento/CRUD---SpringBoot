package CrudSpringBoot.demo.service;

import org.springframework.stereotype.Service;

import CrudSpringBoot.demo.controller.dto.CreateStockDto;
import CrudSpringBoot.demo.entity.Stock;
import CrudSpringBoot.demo.repository.StockRepository;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        
        var stock = new Stock(
            createStockDto.stockId(),
            createStockDto.description()
        );

        stockRepository.save(stock);

    }

}
