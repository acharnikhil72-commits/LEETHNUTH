package com.Hams.Leethnut.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Hams.Leethnut.model.Order1;

@Repository
public interface Repo extends JpaRepository<Order1, Long> {
    @Query("SELECT COUNT(o) FROM Order1 o WHERE o.area = :area AND o.deliveryStatus IS NOT NULL")
    long countConfirmedByArea(@Param("area") String area);

    @Query("SELECT COUNT(o) FROM Order1 o WHERE o.area = :area AND o.deliveryStatus = 'FAILED'")
    long countFailedByArea(@Param("area") String area);

    // NEW: count how many times THIS customer (by area proxy) failed
    // If you have a customerId, use that instead
    @Query("SELECT COUNT(o) FROM Order1 o WHERE o.area = :area AND o.deliveryStatus = 'FAILED'")
    long countCustomerFailures(@Param("area") String area);

}
