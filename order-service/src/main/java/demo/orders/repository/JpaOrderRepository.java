package demo.orders.repository;

import demo.orders.domain.Order;
import java.util.UUID;

import demo.orders.infra.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderJpaEntity, UUID> {}
