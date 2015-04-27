package dk.sdu.mmmi.cbse.core;

import com.decouplink.DisposableList;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_DOWN;
import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.MOVE_UP;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameTime;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Scale;
import dk.sdu.mmmi.cbse.common.data.Velocity;
import dk.sdu.mmmi.cbse.common.data.View;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.PlayN;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.util.Callback;
import playn.core.util.Clock;

public class AsteroidsGame extends Game.Default {

    private final Clock.Source clock = new Clock.Source(33);
    private GroupLayer layer;
    private final Object world = new Object();
    private Entity player;
    

    private final Lookup lookup = Lookup.getDefault();
    private List<IGamePluginService> gamePlugins;

    public AsteroidsGame() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {

        // Add lookup listener
        Lookup.Result<IGamePluginService> result = lookup.lookupResult(IGamePluginService.class);
        result.addLookupListener(lookupListener);
        gamePlugins = new ArrayList<IGamePluginService>(result.allInstances());
        result.allItems();

        // Add clock to world context
        context(world).add(GameTime.class, new GameTime());

        // Keyboard listeners to player
        PlayN.keyboard().setListener(keyboardListener);

        for (IGamePluginService iGamePlugin : gamePlugins) {
            updateGamePlugins(iGamePlugin);
        }

        // create a group layer to hold everything
        layer = graphics().rootLayer();

        // create and add background image layer
//        ImageLayer bg = graphics().createImageLayer().setImage(assets().getImage("assets/images/GrassBackground.png"));
//        layer.add(bg);
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        String url = cl.getResource("assets/images/GrassBackground.png").toExternalForm();

        layer.add(graphics().createImageLayer(assets().getRemoteImage(url, graphics().width(), graphics().height())));
//        layer.add(graphics().createImmediateLayer(
//                new StarRenderer(clock, world)));
    }

    private void updateGamePlugins(IGamePluginService iGamePlugin) {
        // Lookup all Game Plugins using ServiceLoader
        iGamePlugin.start(world);

        for (Entity entity : context(world).all(Entity.class)) {
            if (context(entity).one(EntityType.class) == PLAYER) {
                this.player = entity;
            }
        }
    }

    @Override
    public void update(int delta) {

        clock.update(delta);
        context(world).one(GameTime.class).delta = delta;

        // Process each entity using provided processing services (i.e.,
        // ServiceLoader services)
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            for (Entity e : context(world).all(Entity.class)) {
                //Calls process on the entities which conforms to IEntityProcessingService.
                entityProcessorService.process(world, e);
                //Check for border collision
                Position p = context(e).one(Position.class);
                float height = graphics().height();
                float width = graphics().width();
               
                if (p.x <= 0) {
                    p.x = 0;
                } else if (p.x >= width) {
                    p.x = width;
                }
                if (p.y <= 0) {
                    p.y = 0;
                } else if (p.y >= height) {
                    p.y = height;
                }

            }
        }
    }

    @Override
    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything
        // here!
        clock.paint(alpha);

        for (Entity e : context(world).all(Entity.class)) {

            ImageLayer view = context(e).one(ImageLayer.class);

            Position p = context(e).one(Position.class);
            Rotation r = context(e).one(Rotation.class);
            Scale s = context(e).one(Scale.class);

            if (view == null) {
                view = createView(e);
            }
            view.setTranslation(p.x, p.y);
            view.setRotation(r.angle);
            view.setAlpha(1.0f);
            view.setScale(s.x, s.y);

            if (e.isDestroyed()) {
                layer.remove(view);
                context(world).remove(e);
            }
        }
    }

    private ImageLayer createView(Entity entity) {

        View sprite = context(entity).one(View.class);

        String imagePath = sprite.getImageFilePath();

        Image image = assets().getRemoteImage(imagePath);
        final ImageLayer viewLayer = graphics().createImageLayer(image);

        image.addCallback(new Callback<Image>() {

            @Override
            public void onSuccess(Image result) {
                viewLayer.setOrigin(result.width() / 2f, result.height() / 2f);
            }

            @Override
            public void onFailure(Throwable cause) {
            }
        });

        context(entity).add(ImageLayer.class, viewLayer);
        layer.add(viewLayer);

        return viewLayer;
    }

    //Retrieves the entityProcessingService.
    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private final Keyboard.Listener keyboardListener = new Keyboard.Listener() {

        private final DisposableList disposables = new DisposableList();

        @Override
        public void onKeyDown(Keyboard.Event event) {

            if (event.key() == event.key().W) {
                disposables.add(context(player).add(BehaviourEnum.class, MOVE_UP));
            }

            if (event.key() == event.key().S) {
                disposables.add(context(player).add(BehaviourEnum.class, MOVE_DOWN));
            }

            if (event.key() == event.key().A) {
                disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.MOVE_LEFT));
            }

            if (event.key() == event.key().D) {
                disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.MOVE_RIGHT));
            }

            if (event.key() == event.key().LEFT) {
                disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.TURN_LEFT));
            }

            if (event.key() == event.key().RIGHT) {
                disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.TURN_RIGHT));
            }

            if (event.key() == event.key().SPACE) {
                disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.SHOOT));
            }

//            switch (event.key()) {
//                case W:
//                    disposables.add(context(player).add(BehaviourEnum.class, MOVE_UP));
//                    break;
//
//                case S:
//                    disposables.add(context(player).add(BehaviourEnum.class, MOVE_DOWN));
//                    break;
//
//                case A:
//                    disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.MOVE_LEFT));
//                    break;
//
//                case D:
//                    disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.MOVE_RIGHT));
//                    break;
//                    
//                case LEFT:
//                    disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.TURN_LEFT));
//                    break;
//                    
//                case RIGHT:
//                    disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.TURN_RIGHT));
//                    break;
//
//                case SPACE:
//                    disposables.add(context(player).add(BehaviourEnum.class, BehaviourEnum.SHOOT));
//                    break;
//
//                default:
//                    break;
//            }
        }

        @Override
        public void onKeyTyped(Keyboard.TypedEvent te) {
        }

        @Override
        public void onKeyUp(Keyboard.Event event) {
            disposables.dispose();
        }
    };

    private final LookupListener lookupListener = new LookupListener() {

        //Looks for changes in the every instance of the IGamePluginService
        @Override
        public void resultChanged(LookupEvent le) {
            for (IGamePluginService updatedGamePlugin : lookup.lookupAll(IGamePluginService.class)) {
                if (!gamePlugins.contains(updatedGamePlugin)) {
                    updateGamePlugins(updatedGamePlugin);
                    gamePlugins.add(updatedGamePlugin);
                }
            }
        }
    };

}
