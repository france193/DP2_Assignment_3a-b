package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.NamedEntityReader;

/**
 * Created by FLDeviOS on 23/01/2017.
 */
public class FLNamedEntityReader implements NamedEntityReader {
    private String entityName;

    /**
     * Class' construcotr
     *
     * @param entityName
     */
    FLNamedEntityReader(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Gives the entityName of this entity.
     *
     * @return
     */
    @Override
    public String getName() {
        return this.entityName;
    }

    /**
     * Set the entityName of this entity.
     *
     * @param entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
