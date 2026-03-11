package com.easyplan._04_infra.persistence.orm.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyplan._04_infra.persistence.orm.asset.entity.AccountEntity;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, Long> {

}
