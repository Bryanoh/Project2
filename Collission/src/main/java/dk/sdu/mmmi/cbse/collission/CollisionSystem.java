package dk.sdu.mmmi.cbse.collission;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.HIT;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_DOWN;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_LEFT;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_RIGHT;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_UP;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Radius;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jcs
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionSystem implements IEntityProcessingService {

    @Override
    public void process(Object world, Entity source) {
       
        for (Entity target : context(world).all(Entity.class)) {
            if (!(source.equals(target)) && testCollision(source, target)) {
                
                if (context(source).one(EntityType.class).equals(BULLET)) {
                    context(target).add(BehaviourEnum.class, HIT);
                } else {
                    context(target).add(BehaviourEnum.class, BehaviourEnum.COLLISSION);
                    
                }
                
            }
        }
    }

    private boolean testCollision(Entity source, Entity target) {

        Position srcPos = context(source).one(Position.class);
        Radius srcRadius = context(source).one(Radius.class);

        Position targetPos = context(target).one(Position.class);
        Radius targetRadius = context(target).one(Radius.class);

        float dx = srcPos.x - targetPos.x;
        float dy = srcPos.y - targetPos.y;

        double dist = Math.sqrt((dx * dx) + (dy * dy));
        boolean isCollision = dist <= srcRadius.value
                + targetRadius.value;

        if (isCollision) {
            System.out.println(String.format(
                    "%s hits %s, dist=%s, totalRadius=%s", source, target,
                    dist, srcRadius.value
                    + targetRadius.value));
        }

        return isCollision;
    }

}
