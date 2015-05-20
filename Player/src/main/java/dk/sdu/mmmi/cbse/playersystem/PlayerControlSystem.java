package dk.sdu.mmmi.cbse.playersystem;

import com.decouplink.Context;
import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.collission.CollisionSystem;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BUFFPLAYER;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import static dk.sdu.mmmi.cbse.common.data.EntityType.WEAPONPICKUP;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static dk.sdu.mmmi.cbse.common.utils.EntityFactoryUtil.createBullet;
import static dk.sdu.mmmi.cbse.common.utils.EntityFactoryUtil.createBullet2;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jcs
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class PlayerControlSystem implements IEntityProcessingService {

    ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
    String url = cl.getResource("assets/images/PlayerUpgraded.png").toExternalForm();
    EntityPlugin entPlug = new EntityPlugin();

    DisposableList entities = new DisposableList();

    @Override
    public void process(Object world, Entity entity) {

        Context entityCtx = context(entity);
        Position position = context(entity).one(Position.class);
        Rotation rotation = entityCtx.one(Rotation.class);
        Velocity velocity = entityCtx.one(Velocity.class);
        CollisionSystem collision = new CollisionSystem();

        double thrust = 5.0;

        if (entityCtx.one(EntityType.class).equals(PLAYER) || entityCtx.one(EntityType.class).equals(BUFFPLAYER)) {

            for (BehaviourEnum behaviour : context(entity).all(BehaviourEnum.class)) {

                if (behaviour == behaviour.MOVE_UP) {
                    position.x += Math.cos(rotation.angle) * thrust;
                    position.y += Math.sin(rotation.angle) * thrust;

                    for (Entity e : context(world).all(Entity.class)) {
                        if (!(entity.equals(e)) && collision.testCollision(entity, e) && !context(e).one(EntityType.class).equals(WEAPONPICKUP)) {
                            position.x -= Math.cos(rotation.angle) * thrust;
                            position.y -= Math.sin(rotation.angle) * thrust;
                        }
                    }
                }

                if (behaviour == behaviour.MOVE_DOWN) {
                    position.x -= Math.cos(rotation.angle) * thrust;
                    position.y -= Math.sin(rotation.angle) * thrust;

                    for (Entity e : context(world).all(Entity.class)) {
                        if (!(entity.equals(e)) && collision.testCollision(entity, e) && !context(e).one(EntityType.class).equals(WEAPONPICKUP)) {
                            position.x += Math.cos(rotation.angle) * thrust;
                            position.y += Math.sin(rotation.angle) * thrust;
                        }
                    }
                }

                if (behaviour == behaviour.MOVE_LEFT) {
                    position.x -= Math.cos(rotation.angle + 90) * thrust;
                    position.y -= Math.sin(rotation.angle + 90) * thrust;

                    for (Entity e : context(world).all(Entity.class)) {
                        if (!(entity.equals(e)) && collision.testCollision(entity, e) && !context(e).one(EntityType.class).equals(WEAPONPICKUP)) {
                            position.x += Math.cos(rotation.angle + 90) * thrust;
                            position.y += Math.sin(rotation.angle + 90) * thrust;
                        }
                    }
                }

                if (behaviour == behaviour.MOVE_RIGHT) {
                    position.x += Math.cos(rotation.angle + 90) * thrust;
                    position.y += Math.sin(rotation.angle + 90) * thrust;

                    for (Entity e : context(world).all(Entity.class)) {
                        if (!(entity.equals(e)) && collision.testCollision(entity, e) && !context(e).one(EntityType.class).equals(WEAPONPICKUP)) {
                            position.x -= Math.cos(rotation.angle + 90) * thrust;
                            position.y -= Math.sin(rotation.angle + 90) * thrust;
                        }
                    }
                }

                if (behaviour == behaviour.SHOOT) {
                    if (entityCtx.one(EntityType.class).equals(PLAYER)) {
                    Entity e = createBullet(entity);
                    context(world).add(Entity.class, e);
                    }
                    else {
                         Entity e = createBullet(entity);
                         context(world).add(Entity.class, e);
                         Entity e2 = createBullet2(entity);
                         context(world).add(Entity.class, e2);
                    }
                    entityCtx.remove(behaviour);
                }

                if (behaviour == behaviour.HIT) {
                    Health h = entityCtx.one(Health.class);

                    // Damage
                    h.addDamage(1);

                    // Check for destroyed
                    if (!h.isAlive()) {
                        entity.setDestroyed(true);
                    }

                    // Remove hit behaviour
                    entityCtx.remove(behaviour);
                }

                if (behaviour == behaviour.TURN_LEFT) {
                    rotation.angle -= 0.1;
                }
                if (behaviour == behaviour.TURN_RIGHT) {
                    rotation.angle += 0.1;
                }
                if (behaviour == behaviour.PICKUPPLAYER) {
                    //Weapon pickup here.
                    Position oldPosition =  entityCtx.one(Position.class);
                    Rotation oldRotation = entityCtx.one(Rotation.class);
                    entity.setDestroyed(true);                    
                   
                    System.out.println("PickUp");
 
                    Link<Entity> pl = context(world).add(Entity.class, entPlug.createPlayerShip(url,oldPosition,BUFFPLAYER,oldRotation ));
                    entities.add(pl);

                    
                    
                     entityCtx.remove(behaviour);
                    

                }
            }
        }
    }
}
