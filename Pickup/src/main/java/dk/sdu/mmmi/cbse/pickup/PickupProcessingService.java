package dk.sdu.mmmi.cbse.pickup;

import com.decouplink.Context;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.PICKUP;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.WEAPONPICKUP;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
public class PickupProcessingService implements IEntityProcessingService {

    @Override
    public void process(Object world, Entity entity) {

        Context entityCtx = context(entity);

        if (entityCtx.one(EntityType.class).equals(WEAPONPICKUP)) {

            for (BehaviourEnum behaviour : entityCtx.all(BehaviourEnum.class)) {

                if (behaviour.equals(PICKUP)) {
                    entity.setDestroyed(true);
                }

            }

        }
    }

}
