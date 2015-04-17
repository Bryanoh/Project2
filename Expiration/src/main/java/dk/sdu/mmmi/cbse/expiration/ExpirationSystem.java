package dk.sdu.mmmi.cbse.expiration;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.Expires;
import dk.sdu.mmmi.cbse.common.data.GameTime;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jcs
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class ExpirationSystem implements IEntityProcessingService {

    @Override
    public void process(Object world, Entity entity) {

        Expires expires = context(entity).one(Expires.class);

        if (expires != null) {
            int delta = context(world).one(GameTime.class).delta;
            expires.reduceLifeTime(delta);

            if (expires.isExpired()) {
                entity.setDestroyed(true);
            }
        }
    }
}