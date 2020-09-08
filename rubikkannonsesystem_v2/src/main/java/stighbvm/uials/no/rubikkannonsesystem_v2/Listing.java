/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Stigus
 */
@Entity @Table(name = "ALISTING")
@Data @AllArgsConstructor @NoArgsConstructor
public class Listing {
    public static final String BUYER = "buyer";
    
    @Id
    Long buyerid;
    
            
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
            
}
