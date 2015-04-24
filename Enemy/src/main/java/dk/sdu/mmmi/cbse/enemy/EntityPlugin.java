/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_RANDOM;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.NA;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Radius;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.data.View;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jesper
 */
@ServiceProvider(service = IGamePluginService.class)
public class EntityPlugin implements IGamePluginService {

    DisposableList entities = new DisposableList();
    
    public EntityPlugin() {
        
    }
    
    @Override
    public void start(Object world) {
        // Add entities to the world
    Link<Entity> pl = context(world).add(Entity.class, createEnemyShip());
        entities.add(pl);
    }
    
    public Entity createEnemyShip() {
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/Enemy.png").toExternalForm();

        Entity enemyShip = new Entity();
        context(enemyShip).add(EntityType.class, ENEMY);
        context(enemyShip).add(Health.class, new Health(5));
        context(enemyShip).add(BehaviourEnum.class, MOVE_RANDOM);
        context(enemyShip).add(View.class, new View(url));
        context(enemyShip).add(Position.class, new Position(100, 250));
        context(enemyShip).add(Rotation.class, new Rotation());
        context(enemyShip).add(Velocity.class, new Velocity(0, 0));
        context(enemyShip).add(Scale.class, new Scale());
        context(enemyShip).add(Radius.class, new Radius(10));

        return enemyShip;
    }

    @Override
    public void stop() {
        entities.dispose();
    }
    
}
