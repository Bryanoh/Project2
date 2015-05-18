
package dk.sdu.mmmi.cbse.asteroids;

import com.decouplink.Context;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.COLLISION;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.HIT;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_RANDOM;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = IEntityProcessingService.class)
public class AsteroidsProcessingService implements IEntityProcessingService {
    
    @Override
    public void process(Object world, Entity entity) {
        
        Context entityCtx = context(entity);              

        if (entityCtx.one(EntityType.class).equals(COLLISION)) {

        }
    }
    
}
