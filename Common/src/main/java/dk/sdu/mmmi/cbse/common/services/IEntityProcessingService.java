package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;

public interface IEntityProcessingService {

    void process(Object world, Entity entity);
}
