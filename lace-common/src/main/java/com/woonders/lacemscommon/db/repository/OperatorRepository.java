package com.woonders.lacemscommon.db.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.woonders.lacemscommon.db.entity.Operator;
import com.woonders.lacemscommon.db.entity.Role;

/**
 * Created by Emanuele on 14/11/2016.
 */
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long>, QueryDslPredicateExecutor<Operator> {

	Operator findByUsername(String username);

	Operator findByUsernameAndPassword(String username, String password);

	List<Operator> findByRole(Role role);

	List<Operator> findByRole_RoleName(Role.RoleName roleName);

	@Query("select o.email from Operator o where o.username = :username")
	String findEmailByUsername(@Param("username") String username);

	@Query("select distinct operator from Operator operator join operator.role role where role.roleName not in :roleToAvoidCollection")
	List<Operator> findByRoleNotIn(@Param("roleToAvoidCollection") Collection<Role.RoleName> roleToAvoidCollection);

	List<Operator> findByReceiveLeadEnabledTrueAndAzienda_Id(long aziendaId);

	/**
	 * @return Il prossimo operatore che deve ricevere il nuovo lead, ordinati
	 *         per quanti lead hanno gia ricevuto, in base anche all azienda id
	 *         per il multiagenzia, altrimenti ricevera sempre 1 come parametro
	 */
	Operator findTop1ByReceiveLeadEnabledTrueAndAzienda_IdOrderByLeadRicevutiAsc(long aziendaId);

	List<Operator> findByAzienda_Id(long aziendaId);

	@Transactional
	@Modifying
	@Query("delete from Operator o where o.id = :id")
	void deleteByIdIgnoreCaseAndTipoIgnoreCase(@Param("id") Long id);
}
