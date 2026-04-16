package vn.uit.edu.msshop.inventory.dbcleaner;

import vn.uit.edu.msshop.inventory.adapter.out.persistence.SpringDataInventoryJpaRepository;

//@Component
public class DBCleaner {
    private SpringDataInventoryJpaRepository repository;
    public DBCleaner(SpringDataInventoryJpaRepository repository) {
        System.out.println("Cleared databaseeeeeeeeeeee");
        this.repository= repository;
        repository.deleteAll();
    }
}
