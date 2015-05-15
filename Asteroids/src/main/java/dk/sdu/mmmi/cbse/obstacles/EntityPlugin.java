package dk.sdu.mmmi.cbse.obstacles;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import static dk.sdu.mmmi.cbse.obstacles.EntityFactory.createAsteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)
public class EntityPlugin implements IGamePluginService {
    
    private Link<Entity> pl;
    
    @Override
    public void start(Object world) {
        // Add entities to the world
        for (int i = 0; i < 11; i++) {
            Entity en = createAsteroid();
            pl = context(world).add(Entity.class, en);
            
        }
    }
    
    @Override
    public void stop() {
        pl.getDestination().setDestroyed(true);
    }
    
}
