package com.example.filmBooking.repository;

import com.example.filmBooking.model.Customer;
import com.example.filmBooking.model.RankCustomer;
import com.example.filmBooking.model.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("Select custom From Customer custom Where custom.email = :email")
    Customer findEmail(@Param("email") String email);

    // danh sách khách hàng được sử dụng 1 khuyến mãi
    @Query(value = "SELECT c.* FROM customer c " +
            "INNER JOIN rank_customer r ON c.rank_customer_id = r.id " +
            "INNER JOIN promotion p ON p.rank_customer_id = r.id " +
            "WHERE p.id = ?1 ", nativeQuery = true)
    List<Customer> findByPromotion(String idPromotion);

    Page<Customer> findByNameContains(String keyword, Pageable pageable);

    @Query("SELECT c.point FROM Customer c WHERE c.id = :customerId")
    Integer findPointById(@Param("customerId") String customerId);

    String customer = ("SELECT sum(b.point), SUM(b.total_money) AS total_money_sum, r.name, c.point, sum(b.use_points)\n" +
            "FROM datn.customer c\n" +
            "JOIN datn.rank_customer r ON c.rank_customer_id = r.id\n" +
            "JOIN datn.bill b ON c.id = b.customer_id\n" +
            "WHERE c.id = :customerId and b.status = 1");

    @Query(value = customer, nativeQuery = true)
    List<Object[]> getCustommerById(@Param("customerId") String customerId);

    String point = ("SELECT r.point \n" +
            "FROM datn.rank_customer r\n" +
            "WHERE r.point > \n" +
            "(SELECT point \n" +
            "FROM datn.rank_customer r\n" +
            "WHERE r.id = \n" +
            "(SELECT rank_customer_id\n" +
            "FROM datn.customer c \n" +
            "WHERE c.id = :customerId)\n" +
            ")\n" +
            "ORDER BY point \n" +
            "LIMIT 1;");
    @Query(value = point, nativeQuery = true)
    List<Integer> getPoint(@Param("customerId") String customerId);

    Customer findByEmail(String email);

    @Query(" select sum (b.point) from Bill b where b.customer.id =:customerId and b.status=1")
    Integer pointSetRank(String customerId);

}
