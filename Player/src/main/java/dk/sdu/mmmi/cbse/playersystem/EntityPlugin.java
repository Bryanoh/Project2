package dk.sdu.mmmi.cbse.playersystem;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.NA;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
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

@ServiceProvider(service = IGamePluginService.class)
public class EntityPlugin implements IGamePluginService {

    DisposableList entities = new DisposableList();

    public EntityPlugin() {
    }

    @Override
    public void start(Object world) {
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/Player.png").toExternalForm();
        // Add entities to the world
        Link<Entity> pl = context(world).add(Entity.class, createPlayerShip(url, new Position(1100,900), PLAYER, new Rotation()));
        entities.add(pl);
    }

    public Entity createPlayerShip(String url, Position pos, EntityType type, Rotation r) {

//        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
//        String url = cl.getResource("assets/images/Player.png").toExternalForm();
        Entity playerShip = new Entity();
        context(playerShip).add(EntityType.class, type);
        context(playerShip).add(Health.class, new Health(5));
        context(playerShip).add(BehaviourEnum.class, NA);
        context(playerShip).add(View.class, new View(url));
        context(playerShip).add(Position.class, pos);
        context(playerShip).add(Rotation.class, r);
        context(playerShip).add(Velocity.class, new Velocity(0, 0));
        context(playerShip).add(Scale.class, new Scale());
        context(playerShip).add(Radius.class, new Radius(40));

        return playerShip;
    }

    @Override
    public void stop() {
        // Remove entities
        entities.dispose();
    }

}
