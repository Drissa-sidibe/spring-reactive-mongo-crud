package com.saraya.utils;

import com.saraya.dto.ProductDto;
import com.saraya.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto ConvertEntityToDto(Product product){

        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product , productDto);
        return productDto;
    }
    public static Product ConverDToToEntity(ProductDto productDto){

        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }

}
