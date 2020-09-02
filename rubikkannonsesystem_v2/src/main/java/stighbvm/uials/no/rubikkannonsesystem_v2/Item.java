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
/**
 *
 * @author Stigus
 * An item to be sold in the Fant webstore */
@Entity @Table(name = "AITEM")
@Data @AllArgsConstructor @NoArgsConstructor
public class Item {
    
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
