
package dk.sdu.mmmi.cbse.asteroids;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import static dk.sdu.mmmi.cbse.asteroids.EntityFactory.createAsteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = IGamePluginService.class)
public class EntityPlugin implements IGamePluginService {

    DisposableList entities = new DisposableList();
  
    @Override
    public void start(Object world) {
          // Add entities to the world
        Entity en = createAsteroid();
        Link<Entity> el = context(world).add(Entity.class, en);
        entities.add(el);
    }

    @Override
    public void stop() {
        entities.dispose();
    }
    
}

