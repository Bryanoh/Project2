/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.buff;

import com.decouplink.Context;
import com.decouplink.Link;
import static com.decouplink.Utilities.context;
import static dk.sdu.mmmi.cbse.buff.BuffFactory.createWeaponPickUp;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.PICKUP;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.WEAPONPICKUP;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class BuffProcessing implements IEntityProcessingService{

    @Override
    public void process(Object world, Entity entity) {
        Context entityCtx = context(entity);

        if (entityCtx.one(EntityType.class).equals(WEAPONPICKUP)) {

            for (BehaviourEnum behaviour : entityCtx.all(BehaviourEnum.class)) {

                if (behaviour.equals(PICKUP)) {
                    entity.setDestroyed(true);
                    Entity a = createWeaponPickUp();
                    Link<Entity> el = context(world).add(Entity.class, a);
                }

            }

        }
    }
    
}
