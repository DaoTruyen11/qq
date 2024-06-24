package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
