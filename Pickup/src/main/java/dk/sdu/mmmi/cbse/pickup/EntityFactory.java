package dk.sdu.mmmi.cbse.pickup;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.WEAPONPICKUP;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Radius;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.data.View;
import java.util.Random;
import org.openide.util.Lookup;


public class EntityFactory {
    
    public static Entity createWeaponPickUp(){
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/PODADO.png").toExternalForm();
        
        Entity weaponPickUp = new Entity();
        Random rand = new Random();
        context(weaponPickUp).add(EntityType.class, WEAPONPICKUP);
        context(weaponPickUp).add(View.class, new View(url));
        context(weaponPickUp).add(Position.class, new Position(rand.nextInt(1024), rand.nextInt(768)));
        context(weaponPickUp).add(Rotation.class, new Rotation());
        context(weaponPickUp).add(Velocity.class, new Velocity(0, 0));
        context(weaponPickUp).add(Scale.class, new Scale());
        context(weaponPickUp).add(Radius.class, new Radius(25));
        
        return weaponPickUp;
    }
}
