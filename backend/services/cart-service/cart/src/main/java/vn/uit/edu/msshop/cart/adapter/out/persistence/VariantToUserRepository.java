package vn.uit.edu.msshop.cart.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantToUserRepository extends MongoRepository<VariantToUserRedisModel, String>  {

}
