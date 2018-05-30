package com.project.repository;


import com.project.domain.Shipment;
import com.project.domain.Vendor;
import com.project.service.dto.InboundPackagesDTO;
import com.project.service.dto.PackageInfoDTO;
import com.project.service.dto.PackageStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import com.project.service.dto.PackageDTO;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;




/**
 * Spring Data JPA repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor {

    @Query("select sh.senderV from Shipment sh inner join Vendor v on sh.senderV.id =:id")
    Vendor jeta(@Param("id") Long id);




    @Query("select new com.project.service.dto.PackageDTO(p.id,s.id,r.id,pr.id,s.statusName,r.fullName,r.address,r.zipCode,pd.width,pd.length,pd.height,pd.weight,wh.unit,pt.type,pr.price)" +
        "from Shipment sh" +
        " inner join sh.status s " +
        " inner join sh.receiver r" +
        " inner join sh.details pd "+
        " inner join pd.weightUnit wh "+
        " inner join pd.type pt "+
        "inner join pd.product p " +
        " inner join p.price pr " +
        " where senderp_id=:senderp_id")
    List<PackageDTO> getAllRecordsBySender(@Param("senderp_id") Long senderpId);


    @Query("select new com.project.service.dto.PackageDTO(p.id,s.id,r.id,pr.id,s.statusName,r.fullName,r.address,r.zipCode,pd.width,pd.length,pd.height,pd.weight,wh.unit,pt.type,pr.price)" +
        "from Shipment sh" +
        " inner join sh.status s " +
        " inner join sh.receiver r" +
        " inner join sh.details pd "+
        " inner join pd.weightUnit wh "+
        " inner join pd.type pt "+
        "inner join pd.product p " +
        " inner join p.price pr " +
        " where senderp_id=:senderp_id and p.id=:productid")
    PackageDTO getAllByPersonAndProduct(@Param("productid")Long productid,@Param("senderp_id") Long person_id);

    @Query("select new com.project.service.dto.PackageStatusDTO(p.id,pr.id,s.id,e.id,e.name,s.statusName,pr.price)" +
        "from Shipment sh " +
        " inner join sh.deliverEmployee e "+
        " inner join sh.status s" +
        " inner join sh.product p" +
        " inner join p.price pr" +
        " where senderp_id=:senderp_id and p.id=:productId")
    PackageStatusDTO getPackageDetails(@Param("senderp_id") Long senderp_id,@Param("productId") Long productid);

    @Query("select new com.project.service.dto.PackageInfoDTO(p.id,pr.price,d.id,d.name,d.email,d.tel,c.id,c.name,c.email,c.tel)" +
        " from Shipment  sh" +
        " inner join Employee d on (sh.deliverEmployee.id = d.id) "+
        " inner join Employee c on (sh.contactEmployee.id = c.id) "+
        " inner join sh.product p"+
        " inner join p.price pr "+
        "where senderp_id=:person_id and p.id=:product_id")
    PackageInfoDTO getPackageInfo(@Param("person_id") Long person_id,@Param("product_id") Long product_id);

    @Query("select new com.project.service.dto.InboundPackagesDTO (p.fullName,p.email,r.fullName,r.address,d.name,d.tel,c.name,c.tel,st.id,st.statusName,pr.id,pri.price,l.id) "+
        " from Shipment sh " +
        "inner join sh.senderP p " +
        "inner join sh.receiver r " +
        " inner join Employee d on (sh.deliverEmployee.id = d.id) "+
        " inner join Employee c on (sh.contactEmployee.id = c.id) "+
        "inner join sh.status st " +
        "inner join sh.location l " +
        "inner join sh.product pr " +
        "inner join pr.price pri " +
        "where sh.status.id=1 ")
    Page<InboundPackagesDTO> getInboundPackages(Pageable pageable);
}
