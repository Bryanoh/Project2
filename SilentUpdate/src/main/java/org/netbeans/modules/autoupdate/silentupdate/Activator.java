package org.netbeans.modules.autoupdate.silentupdate;

/**
 *
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Activator extends ModuleInstall {

    private final ScheduledExecutorService exector = Executors.newScheduledThreadPool(1);
    private JTextField txt1;
    private String componentName;

    @Override
    public void restored() {
        exector.scheduleAtFixedRate(doCheck, 10000, 10000, TimeUnit.MILLISECONDS);
        disableFrame();
    }

    private static final Runnable doCheck = new Runnable() {
        @Override
        public void run() {
            if (UpdateHandler.timeToCheck()) {
                UpdateHandler.checkAndHandleUpdates();
            }
        }

    };

    public void disableFrame() {

        JFrame frame = new JFrame("Disable a module");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btn1 = new JButton("Disable the module");
        JButton btn2 = new JButton("Enable the module");
        txt1 = new JTextField(20);
        JLabel lblComponents = new JLabel();

        frame.add(btn1, BorderLayout.SOUTH);
        frame.add(txt1, BorderLayout.CENTER);
        frame.add(lblComponents, BorderLayout.NORTH);
        frame.add(btn2, BorderLayout.EAST);

        lblComponents.setText("<html>Press a the corresponding number to disable a module: " + "\n" + "1: Collision " + "\n" + "2: Enemy " + "\n" + "3: Expiration" + "\n"
                + "4: Move" + "\n" + "5: Obstacles " + "\n" + "6: Player " + "\n" + "</html>");

        frame.setSize(400, 400);

        frame.setVisible(true);

        btn1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                componentName = "";

                componentName = txt1.getText();
                int moduleChosen = Integer.parseInt(componentName);
                String chosenModule = "";

                switch (moduleChosen) {
                    case 1:
                        chosenModule = "Collision";
                        break;
                    case 2:
                        chosenModule = "Enemy";
                        break;
                    case 3:
                        chosenModule = "Expiration";
                        break;
                    case 4:
                        chosenModule = "Move";
                        break;
                    case 5:
                        chosenModule = "Obstacles";
                        break;
                    case 6:
                        chosenModule = "Player";
                        break;
                    default:
                        OutputLogger.log("You need to chose a valid module to disable");
                        break;

                }

                UpdateHandler.doDisable(chosenModule);
            }

        });

        btn2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) throws NumberFormatException {
                componentName = "";

                componentName = txt1.getText();
                int moduleChosen = Integer.parseInt(componentName);
                String chosenModule = "";
                try {
                    switch (moduleChosen) {
                        case 1:
                            chosenModule = "Collision";
                            break;
                        case 2:
                            chosenModule = "Enemy";
                            break;
                        case 3:
                            chosenModule = "Expiration";
                            break;
                        case 4:
                            chosenModule = "Move";
                            break;
                        case 5:
                            chosenModule = "Obstacles";
                            break;
                        case 6:
                            chosenModule = "Player";
                            break;
                        default:
                            OutputLogger.log("You need to chose a valid module to disable");
                            break;
                    }
                } catch (NumberFormatException ex) {
                    OutputLogger.log("You need to choose a valid number");
                }
            

            UpdateHandler.doInstall(chosenModule);
            }

        }
        );

    }

}
