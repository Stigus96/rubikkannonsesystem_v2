package stighbvm.uials.no.rubikkannonsesystem_v2;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import stighbvm.uials.no.rubikkannonsesystem_v2.Group;

/**
 *
 * @author mikael
 */
@Singleton
@Startup
public class AuthRunOnStartup {
    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void init() {
        long groups = (long) em.createQuery("SELECT count(g.name) from Group g").getSingleResult();
        if(groups == 0) {
            em.persist(new Group(Group.USER));
            em.persist(new Group(Group.ADMIN));
        }
    }
}
