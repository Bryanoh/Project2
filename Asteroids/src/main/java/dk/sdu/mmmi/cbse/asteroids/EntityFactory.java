
package dk.sdu.mmmi.cbse.asteroids;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_RANDOM;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import static dk.sdu.mmmi.cbse.common.data.EntityType.WEAPONPICKUP;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Radius;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.data.View;
import java.util.Random;
import org.openide.util.Lookup;


public class EntityFactory {
    
    public static Entity createAsteroid() {
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/Obstacle3.png").toExternalForm();
        
    Entity asteroid = new Entity();
    Random rand = new Random();
        context(asteroid).add(EntityType.class, ASTEROIDS);
//        context(asteroid).add(Health.class, new Health(2));
//        context(asteroid).add(BehaviourEnum.class, MOVE_RANDOM);
        context(asteroid).add(View.class, new View(url));
        context(asteroid).add(Position.class, new Position(rand.nextInt(1000), rand.nextInt(1000)));
//        context(asteroid).add(Position.class, new Position(40, 40));
        context(asteroid).add(Rotation.class, new Rotation());
        context(asteroid).add(Velocity.class, new Velocity(0, 0));
        context(asteroid).add(Scale.class, new Scale());
        context(asteroid).add(Radius.class, new Radius(10));
        return asteroid;
    }
    
    public static Entity createWeaponPickUp(){
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/Potato.png").toExternalForm();
        
        Entity weaponPickUp = new Entity();
        Random rand = new Random();
        context(weaponPickUp).add(EntityType.class, WEAPONPICKUP);
        context(weaponPickUp).add(View.class, new View(url));
        context(weaponPickUp).add(Position.class, new Position(rand.nextInt(1000), rand.nextInt(1000)));
        context(weaponPickUp).add(Rotation.class, new Rotation());
        context(weaponPickUp).add(Velocity.class, new Velocity(0, 0));
        context(weaponPickUp).add(Scale.class, new Scale());
        context(weaponPickUp).add(Radius.class, new Radius(10));
        
        return weaponPickUp;
    }
}
