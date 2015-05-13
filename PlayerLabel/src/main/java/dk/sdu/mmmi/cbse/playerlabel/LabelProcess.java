/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.playerlabel;

import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import javax.swing.JLabel;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class LabelProcess implements IEntityProcessingService{
    JLabel playerHp = new JLabel();
    @Override
    public void process(Object world, Entity entity) {
       playerHp.setLocation(10, 10);
       if(context(entity).one(EntityType.class).equals(PLAYER)) {
           playerHp.setText("PlayerHP = " + context(entity).one(Health.class).getHealth());
       } 
        
    }
    
}
