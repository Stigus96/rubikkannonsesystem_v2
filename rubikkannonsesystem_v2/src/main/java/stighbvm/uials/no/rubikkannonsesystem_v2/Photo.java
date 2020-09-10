/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stighbvm.uials.no.rubikkannonsesystem_v2;

import javax.persistence.Basic;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
/**
 *
 * @author Stigus
 * Represents a photo or an image of an item for sale */
    //@NamedQuery(name = FIND_BY_NAME_AND_WIDTH,
            //query = "select * from Photos")
    
@Entity
public class Photo {
public static final String FIND_BY_NAME = "Photo.findByName";

@Id
Long id;

String name;

@Lob
@Basic(fetch = FetchType.LAZY)
private byte[] photo;
        
}
