
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.buff;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import static dk.sdu.mmmi.cbse.buff.BuffFactory.createWeaponPickUp;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IGamePluginService.class)
public class BuffPlugin implements IGamePluginService{
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
