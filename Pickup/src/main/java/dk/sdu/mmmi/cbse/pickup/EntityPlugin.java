package dk.sdu.mmmi.cbse.pickup;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import static dk.sdu.mmmi.cbse.pickup.EntityFactory.createWeaponPickUp;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)
public class EntityPlugin implements IGamePluginService {

    DisposableList entities = new DisposableList();

    @Override
    public void start(Object world) {
        // Add entities to the world
       
        Entity a = createWeaponPickUp();
        Link<Entity> el = context(world).add(Entity.class, a);
        entities.add(el);
    }

    @Override
    public void stop() {
        entities.dispose();
    }

}
