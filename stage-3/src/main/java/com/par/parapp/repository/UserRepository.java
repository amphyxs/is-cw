package com.par.parapp.repository;

import com.par.parapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "SELECT user_login_update(:arg_login, 'active');", nativeQuery = true)
    void loginAsUser(@Param("arg_login") String login);

    @Transactional
    @Modifying
    @Query(value = "SELECT user_login_update(:arg_login, 'inactive');", nativeQuery = true)
    void logoutFromUser(@Param("arg_login") String login);

    @Transactional
    @Modifying
    @Query(value = "SELECT add_wallet_balance((SELECT wallet_id FROM Users WHERE login = :arg_login), :arg_balance);", nativeQuery = true)
    void replenishBalance(@Param("arg_login") String login, @Param("arg_balance") Double balance);

    @Transactional
    @Modifying
    @Query(value = "SELECT add_wallet_balance((SELECT wallet_id FROM Users WHERE login = :arg_login), :arg_balance);", nativeQuery = true)
    void replenishBalanceSeller(@Param("arg_login") String login, @Param("arg_balance") Double balance);

    @Transactional
    @Modifying
    @Query(value = "SELECT deduct_wallet_balance((SELECT wallet_id FROM Users WHERE login = :arg_login), :arg_balance);", nativeQuery = true)
    void chargeBalanceCustomer(@Param("arg_login") String login, @Param("arg_balance") Double balance);

    @Query(value = "SELECT balance FROM Wallet WHERE id = (SELECT wallet_id FROM Users WHERE login = :arg_login);", nativeQuery = true)
    Double getBalance(@Param("arg_login") String login);
}
