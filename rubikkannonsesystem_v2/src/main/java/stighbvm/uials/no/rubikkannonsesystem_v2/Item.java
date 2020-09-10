/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static stighbvm.uials.no.rubikkannonsesystem_v2.Item.FIND_ALL_ITEMS;
import static stighbvm.uials.no.rubikkannonsesystem_v2.Item.FIND_BY_ITEMID;
/**
 *
 * @author Stigus
 * An item to be sold in the Fant webstore */
@Entity @Table(name = "AITEM")
@Data @AllArgsConstructor @NoArgsConstructor
@NamedQuery(name = FIND_ALL_ITEMS, query = "select i from Item order by i.title")
@NamedQuery(name = FIND_BY_ITEMID,
        query = "select distinct i from Item i, User u" +
                "where i.itemid = :itemid and u.userid = :userid")

public class Item {
    public static final String FIND_ALL_ITEMS = "Item.findAllItems";
    public static final String FIND_BY_ITEMID = "Item.findByItemid";
    

    
    @Id @GeneratedValue
    Long itemid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created;
    
    @ManyToMany
    @JoinTable(name="AUSERGROUP",
            joinColumns = @JoinColumn(name="userid", referencedColumnName = "userid"),
            inverseJoinColumns = @JoinColumn(name="name",referencedColumnName = "name"))
    List<Group> groups;

    @NotBlank(message = "Title cannot be blank")
    String title;
    
    String description;
    
    @NotNull(message = "Price cannot be null") @Positive(message = "price must be a positive value")
    BigDecimal price;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "aitem_properties", joinColumns=@JoinColumn(name="itemid"))
    @MapKeyColumn(name="key")
    @Column(name = "value")
    Map<String,String> properties = new HashMap<>();
    
    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
    
    @OneToOne
    @JoinTable(name="itemid",
            inverseJoinColumns = @JoinColumn (name="itemid", referencedColumnName ="itemid"),
            joinColumns = @JoinColumn(name = "Litemid", referencedColumnName ="Litemid"))
    List<Listing> listings;
    
}
