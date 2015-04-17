package dk.sdu.mmmi.cbse.common.utils;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.DIRECTED;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import dk.sdu.mmmi.cbse.common.data.Expires;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Radius;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.data.View;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.openide.util.Lookup;

/**
 *
 * @author jcs
 */
public class EntityFactoryUtil {

    public static Entity createBullet(Entity source) {
        Rotation r = context(source).one(Rotation.class);
        Position p = context(source).one(Position.class);
        Velocity v = context(source).one(Velocity.class);

        // Calc start position
        float c = 30.0f;
        float x = p.x + (float) (c * cos(r.angle));
        float y = p.y + (float) (c * sin(r.angle));

        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/Bullet.png").toExternalForm();

        Entity bullet = new Entity();
        context(bullet).add(EntityType.class, BULLET);
        context(bullet).add(BehaviourEnum.class, DIRECTED);
        context(bullet).add(View.class, new View(url));
        context(bullet).add(Position.class, new Position(x, y));
        context(bullet).add(Rotation.class, new Rotation(r.angle));

        float velocityX = (float) (v.vectorX + Math.cos(r.angle) * 10);
        float velocityY = (float) (v.vectorY + Math.sin(r.angle) * 10);
        context(bullet).add(Velocity.class, new Velocity(velocityX, velocityY));

        context(bullet).add(Radius.class, new Radius(2));
        context(bullet).add(Scale.class, new Scale());
        context(bullet).add(Expires.class, new Expires(1000));

        return bullet;
    }

}
