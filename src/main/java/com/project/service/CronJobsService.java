package com.project.service;

import com.project.domain.CronJobs;
import com.project.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public interface CronJobsService {


    /**
     * Save a employee.
     *
     * @param cronJobs the entity to save
     * @return the persisted entity
     */
    CronJobs save(CronJobs cronJobs);

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CronJobs> findAll(Pageable pageable);

    /**
     * Get the "id" employee.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CronJobs findOne(Long id);

    /**
     * Delete the "id" employee.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<CronJobs> findAll(CriteriaQuery<CronJobs> query);
}