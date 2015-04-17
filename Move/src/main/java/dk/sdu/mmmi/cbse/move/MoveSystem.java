/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.move;


import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jcs
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MoveSystem implements IEntityProcessingService {

    @Override
    public void process(Object world, Entity entity) {

        Position pos = context(entity).one(Position.class);
        Velocity velocity = context(entity).one(Velocity.class);

        pos.x += velocity.vectorX;
        pos.y += velocity.vectorY;
    }

}
   
