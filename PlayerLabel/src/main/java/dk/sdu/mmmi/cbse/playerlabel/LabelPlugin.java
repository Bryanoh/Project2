/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.playerlabel;

import com.decouplink.DisposableList;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IGamePluginService.class)
public class LabelPlugin implements IGamePluginService{
    DisposableList entities = new DisposableList();
    
    @Override
    public void start(Object world) {
        Entity en = createLabel();
        Link<Entity> el = context(world).add(Entity.class, en);
        entities.add(el);
    }

    @Override
    public void stop() {
        
    }
    public Entity createLabel() {
       
        
        Entity label = new Entity();
        
        context(label).add(EntityType.class, ASTEROIDS);
        context(label).add(Position.class, new Position(10, 10));
        context(label).add(Scale.class, new Scale());
        return label;
    }
    
}
