
package dk.sdu.mmmi.cbse.asteroids;

import com.decouplink.Context;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.HIT;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = IEntityProcessingService.class)
public class ProcessingService implements IEntityProcessingService {
    
    @Override
    public void process(Object world, Entity entity) {
        
        Context entityCtx = context(entity);              

        if (entityCtx.one(EntityType.class).equals(ASTEROIDS)) {
                if(entityCtx.one(BehaviourEnum.class).equals(HIT)) {
                    System.out.println("Obstacle hit");
                }
        }
    }
    
}
