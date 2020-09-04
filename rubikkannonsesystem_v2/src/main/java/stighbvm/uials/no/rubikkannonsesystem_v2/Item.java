/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import java.util.Date;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static stighbvm.uials.no.rubikkannonsesystem_v2.Item.FIND_ALL_ITEMS;
/**
 *
 * @author Stigus
 * An item to be sold in the Fant webstore */
@Entity @Table(name = "AITEM")
@Data @AllArgsConstructor @NoArgsConstructor
@NamedQuery(name = FIND_ALL_ITEMS, query = "select i from Item oredr by i.title")
public class Item {
    public static final String FIND_ALL_ITEMS = "Items.findAllItems";
    
    public enum State {
        ACTIVE, INACTIVE
    }
    
    @Id
    Long itemid;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date created;
    
    String title;
    String description;
    
    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
    
}
