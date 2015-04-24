package dk.sdu.mmmi.cbse.playersystem;

import com.decouplink.Context;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.HIT;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static dk.sdu.mmmi.cbse.common.utils.EntityFactoryUtil.createBullet;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jcs
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(Object world, Entity entity) {

        Position position = context(entity).one(Position.class);
        Rotation rotation = context(entity).one(Rotation.class);
        Velocity velocity = context(entity).one(Velocity.class);
        Health health = context(entity).one(Health.class);

        double thrust = 5.0;

        for (BehaviourEnum behaviour : context(entity).all(BehaviourEnum.class)) {

            if (behaviour == behaviour.MOVE_UP) {
                position.y -= thrust;
            }

            if (behaviour == behaviour.MOVE_DOWN) {
                position.y += thrust;
            }

            if (behaviour == behaviour.MOVE_LEFT) {
                position.x -= thrust;
            }

            if (behaviour == behaviour.MOVE_RIGHT) {
                position.x += thrust;
            }

            if (behaviour == behaviour.SHOOT) {
                Entity e = createBullet(entity);
                context(world).add(Entity.class, e);
                context(world).remove(behaviour);
            }

            if (behaviour == behaviour.HIT) {
                health.addDamage(1);
            }

            if (behaviour == behaviour.TURN_LEFT) {
                rotation.angle -= 0.1;
            }
            if (behaviour == behaviour.TURN_RIGHT) {
                rotation.angle += 0.1;
            }

//            switch (behaviour) {
//                case MOVE_UP:
//                    position.y -= thrust;
//                    break;
//
//                case MOVE_DOWN:
//                    position.y += thrust;
//                    break;
//
//                case MOVE_LEFT:
//                    position.x -= thrust;
//                    break;
//
//                case MOVE_RIGHT:
//                    position.x += thrust;
//                    break;
//
//                case SHOOT:
//                    Entity e = createBullet(entity);
//                    context(world).add(Entity.class, e);
//                    context(world).remove(behaviour);
//                    break;
//                case HIT:
//                    health.addDamage(1);
//                    break;
//                case TURN_LEFT:
//                    rotation.angle -= 0.1;
//                    break;
//                case TURN_RIGHT:
//                    rotation.angle += 0.1;
//                    break;
//                default:
//                    break;
//            }
        }
    }

//    @Override
//    public void process(Object world, Entity entity) {
//
//        Context entityCtx = context(entity);
//        Rotation rotation = entityCtx.one(Rotation.class);
//        Velocity velocity = entityCtx.one(Velocity.class);
//
//        double thrust = .1;
//
//        if (entityCtx.one(EntityType.class).equals(PLAYER)) {
//
//            for (BehaviourEnum behaviour : context(entity).all(BehaviourEnum.class)) {
//
//                switch (behaviour) {
//                    case MOVE_UP:
//                        velocity.vectorX += Math.cos(rotation.angle) * thrust;
//                        velocity.vectorY += Math.sin(rotation.angle) * thrust;
//                        break;
//
//                    case MOVE_DOWN:
//                        velocity.vectorX -= Math.cos(rotation.angle) * thrust;
//                        velocity.vectorY -= Math.sin(rotation.angle) * thrust;
//                        break;
//
//                    case MOVE_LEFT:
//                        rotation.angle -= 0.1;
//                        break;
//
//                    case MOVE_RIGHT:
//                        rotation.angle += 0.1;
//                        break;
//
//                    case SHOOT:
//                        Entity e = createBullet(entity);
//                        context(world).add(Entity.class, e);
//                        entityCtx.remove(behaviour);
//                        break;
//
//                    case HIT:
//
//                        Health h = entityCtx.one(Health.class);
//
//                        // Damage
//                        h.addDamage(1);
//
//                        // Check for destroyed
//                        if (!h.isAlive()) {
//                            entity.setDestroyed(true);
//                        }
//
//                        // Remove hit behaviour
//                        entityCtx.remove(behaviour);
//
//                    default:
//                        break;
//                }
//
//            }
//        }
//    }
}
