package com.invest.investment.repositories.mongo;

import com.invest.investment.entites.mongo.Audit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends MongoRepository<Audit, Long> {}
