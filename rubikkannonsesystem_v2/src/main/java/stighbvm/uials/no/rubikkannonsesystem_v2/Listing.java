/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static stighbvm.uials.no.rubikkannonsesystem_v2.Listing.FIND_BY_LISTINGID;
import static stighbvm.uials.no.rubikkannonsesystem_v2.Listing.FIND_UNSOLD_LISTINGS;

/**
 *
 * @author Stigus
 */
@Entity @Table(name = "ALISTING")
@Data @AllArgsConstructor @NoArgsConstructor
@NamedQuery(name = FIND_BY_LISTINGID,
        query = "query that works perfectly")
@NamedQuery(name = FIND_UNSOLD_LISTINGS, query = "select l from Listing where buyerid is null order by listingid")

public class Listing {
    public static final String BUYER = "buyer";
    public static final String SELLER = "seller";
    public static final String FIND_BY_LISTINGID = "Listing.findByListingId";
    public static final String FIND_UNSOLD_LISTINGS = "Listing.findUnsoldListings";
    
    
    @Id @GeneratedValue
    Long listingid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created;
    
    @OneToMany
    @JoinTable(name="buyerid",
            joinColumns = @JoinColumn(name = "buyerid", referencedColumnName ="buyerid"),
            inverseJoinColumns = @JoinColumn (name="userid", referencedColumnName ="userid"))
    List<User> users;
    
    @OneToOne
    @JoinTable(name="Litemid",
            joinColumns = @JoinColumn(name = "Litemid", referencedColumnName ="Litemid"),
            inverseJoinColumns = @JoinColumn (name="itemid", referencedColumnName ="itemid"))
    List<Item> items;
    
    Long Litemid;
    String sellerid;
    String buyerid;
    
    
            
    
            
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "alisitng_properties", joinColumns=@JoinColumn(name="listing"))
    @MapKeyColumn(name="key")
    @Column(name = "value")
    Map<String,String> properties = new HashMap<>();
    
    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
}
