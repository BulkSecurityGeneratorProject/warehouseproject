package com.project.service;

import com.project.domain.Shipment;
import com.project.domain.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Service Interface for managing Shipment.
 */
public interface ShipmentService {

    /**
     * Save a shipment.
     *
     * @param shipment the entity to save
     * @return the persisted entity
     */
    Shipment save(Shipment shipment);

    /**
     * Get all the shipments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Shipment> findAll(Pageable pageable);

    /**
     * Get the "id" shipment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Shipment findOne(Long id);

    /**
     * Delete the "id" shipment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    boolean shipmentValidation(Shipment shipment) throws Exception;

    List<Shipment> findAll(Specification spec);

    Vendor jeta(Long id);

    List<Shipment> findAll(CriteriaQuery<Shipment> query);
}
