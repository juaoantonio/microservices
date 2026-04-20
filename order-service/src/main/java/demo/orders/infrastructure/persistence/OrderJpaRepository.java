package demo.orders.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
  @EntityGraph(attributePaths = "items")
  Optional<OrderJpaEntity> findOneById(UUID id);
}
