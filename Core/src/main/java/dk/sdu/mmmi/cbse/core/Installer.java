package dk.sdu.mmmi.cbse.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.openide.modules.ModuleInstall;
import playn.core.PlayN;
import playn.java.JavaPlatform;

public class Installer extends ModuleInstall {

    private AsteroidsGame game;
    private ScheduledExecutorService e = Executors.newScheduledThreadPool(1);

    @Override
    public void restored() {

        JavaPlatform.Config config = new JavaPlatform.Config();
        JavaPlatform.register(config);

        game = new AsteroidsGame();

        e.schedule(new Runnable() {
            @Override
            public void run() {
                PlayN.run(game);
            }
        }, 10000, TimeUnit.MILLISECONDS);
    }

}
