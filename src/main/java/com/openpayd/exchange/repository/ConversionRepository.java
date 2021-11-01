package com.openpayd.exchange.repository;

import com.openpayd.exchange.entity.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ConversionRepository extends PagingAndSortingRepository<Conversion, Long> {

    Page<Conversion> findByCreatedDate(Date createdDate, Pageable pageable);

}
