package dk.sdu.mmmi.cbse.SmartEnemy;

import static dk.sdu.mmmi.cbse.common.data.BehaviourEnum.HIT;
import com.decouplink.Context;
import static com.decouplink.Utilities.context;
import dk.sdu.mmmi.cbse.common.data.BehaviourEnum;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.Health;
import dk.sdu.mmmi.cbse.common.data.Position;
import dk.sdu.mmmi.cbse.common.data.Rotation;
import dk.sdu.mmmi.cbse.common.data.Square;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Bryan
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class EnemyProcessingService implements IEntityProcessingService {

// AI FIELDS
    private ArrayList<Square> obstaclesList;
    private ArrayList<Square> grids;
    private ArrayList<Square> openList;
    private ArrayList<Square> closedList;
    private Square parentSquare; // Last position
    private Square targetSquare; // Target
    private Square moveNext; // Next position
    private Position pos;

    // Obstacles
    private boolean allObstaclesLocated = false;
    private boolean allAdjacentObstaclesLocated = false;

    // Prices for moving
    private final int gHorizontal = 10;
    private final int gVertical = 10;
    private final int gDiagonal = 14;
    private final float velocity = 2;

    /**
     * Initialize
     */
    private void init() {
        obstaclesList = new ArrayList<Square>();
        closedList = new ArrayList<Square>();
        openList = new ArrayList<Square>();

        makeGrid();
    }

    /**
     * Make grid out of 1024x768 map
     */
    public void makeGrid() {
        // 768 grids (precalculated based on map 1024x768)
        grids = new ArrayList<Square>(768);
        // Grids are 32x32, 16 points to the middle of a grid.
        int posX = 16;
        int posY = 16;

        do {
            grids.add(new Square(new Position(posX, posY)));
            posX += 32;
            if (posX > 1024) {
                posX = 16;
                posY += 32;
            }
        } while (posX < 1024 && posY < 768);
    }

    /**
     * Calculate cost from this square (A) directly to target square (B)
     * Ignoring Obstacles - ONLY MOVES HORIZONTAL OR VERTICAL
     *
     * @return
     */
    private int calculateH(Square A) {

        int hCost = 0;

        float x = A.pos.x;
        float y = A.pos.y;

        Position current = new Position(x, y);
        Position target = targetSquare.pos;

        // Moves
        int horizontal = 0;
        int vertical = 0;

        while (current.x < target.x || current.y < target.y) {
            // Move Horizontal
            // Right
            if (current.x < target.x) {
                current.x += 32;
                horizontal++;
            }
            // Left
            if (current.x > target.x) {
                current.x -= 32;
                horizontal++;
            }

            // Move Vertical
            // Up
            if (current.y > target.y) {
                current.y -= 32;
                vertical++;
            } // Down
            if (current.y < target.y) {
                current.y += 32;
                vertical++;
            }

        }

        hCost = (gHorizontal * horizontal) + (gVertical * vertical);
        return hCost;
    }

    /**
     * Find adjacent squares in grid from square and add them to listB
     *
     * @param listA
     * @param square
     * @param listB
     */
    public void findAdjacentSquares(Square square, List<Square> listB) {
        for (Square s : grids) {
            // Add adjacent HORIZONTAL grids from parentSquare
            if (Math.abs(s.pos.x - square.pos.x) == 32 && s.pos.y == square.pos.y) {
                s.setGCost(gHorizontal);
                listB.add(s);
            } // Add adjacent VERTICAL grids from parentSquare
            else if (Math.abs(s.pos.y - square.pos.y) == 32 && s.pos.x == square.pos.x) {
                s.setGCost(gVertical);
                listB.add(s);
            } // Add adjacent DIAGONAL grids from parentSquare
            else if (Math.abs(s.pos.x - square.pos.x) == 32 && Math.abs(s.pos.y - square.pos.y) == 32) {
                s.setGCost(gDiagonal);
                listB.add(s);
            }
        }
    }

    /**
     * Find adjacent squares in grid from square and add them to listB Avoiding
     * listC
     *
     * @param listA
     * @param square
     * @param listB
     */
    public void findAdjacentSquares(Square square, List<Square> listB, List<Square> listC) {
        for (Square s : grids) {
            if (!listC.contains(s)) {
                // Add adjacent HORIZONTAL grids from parentSquare
                if (Math.abs(s.pos.x - square.pos.x) == 32 && s.pos.y == square.pos.y) {
                    s.setGCost(gHorizontal);
                    listB.add(s);
                } // Add adjacent VERTICAL grids from parentSquare
                else if (Math.abs(s.pos.y - square.pos.y) == 32 && s.pos.x == square.pos.x) {
                    s.setGCost(gVertical);
                    listB.add(s);
                } // Add adjacent DIAGONAL grids from parentSquare
                else if (Math.abs(s.pos.x - square.pos.x) == 32 && Math.abs(s.pos.y - square.pos.y) == 32) {
                    s.setGCost(gDiagonal);
                    listB.add(s);
                }
            }
        }
    }

    /**
     * Based on A* Pathfinding
     */
    public Square calculateShortestPath(Position own, Position target) {
        for (Square s : grids) {
            // get grid of ownPosition
            if (Math.abs(s.pos.x - own.x) <= 16 && Math.abs(s.pos.y - own.y) <= 16) {
                openList.add(s);
                parentSquare = s;
            }
        }

        // Find adjacent squares and add to openList
        findAdjacentSquares(parentSquare, openList, obstaclesList);
        // Ignores squares with obstacles on

        // Remove parentPostion from openList and put into closedList
        // Squares in closedList are ignored
        closedList.add(openList.remove(openList.indexOf(parentSquare)));

        // Calculate cost for all squares in openList
        for (Square s : openList) {
            s.setTotalCost(s.getGCost(), calculateH(s));
        }

        int lowest = Integer.MAX_VALUE;

        for (Square s : openList) {
            if (!closedList.contains(s)) {
                if (s.getCost() < lowest) {
                lowest = s.getCost();
                moveNext = s;

            }
            }
        }

        closedList.clear();
        openList.clear();

        return moveNext;

    }

    private float calculateAngle() {
        double angle = 0;

        // calculate rotation angle
        // target.x > pos.x
        if (targetSquare.pos.x > pos.x) {
            angle = Math.toDegrees(0);
            return (float) angle;
        } // pos.x > target.x
        else {
            angle = Math.toDegrees(200);
            return (float) angle;
        }
    }

    @Override
    public void process(Object world, Entity entity) {

        Context entityCtx = context(entity);

        // Make grid
        if (grids == null) {
            init();
            System.out.println("---MAKING GRID---");
        }

        // Get all obstacles and their squares at game start
        if (entityCtx.one(EntityType.class).equals(EntityType.OBSTACLES)) {
            if (allObstaclesLocated && !allAdjacentObstaclesLocated) {
                System.out.println("finding adjacent");
                // This code adds all found obstacles to obstacles list, 
                // and then finds all adjacent squares in each obstacle to obstacleList
                ArrayList<Square> obstacles = new ArrayList<Square>();
                for (Square s : obstaclesList) {
                    obstacles.add(s);
                }
                for (Square s : obstacles) {
                    findAdjacentSquares(s, obstaclesList);
                }
                System.out.println("done");
                System.out.println("ObstacleList size: " + obstaclesList.size());
                allAdjacentObstaclesLocated = true;
            } else {
                Position obsPos = entityCtx.one(Position.class);
                for (Square s : grids) {
                    if (Math.abs(s.pos.x - obsPos.x) <= 16 && Math.abs(s.pos.y - obsPos.y) <= 16) {
                        if (obstaclesList.contains(s)) {
                            allObstaclesLocated = true;
                        } else {
                            obstaclesList.add(s);
                        }
                    }
                }
            }
        }

        // Locate target
        if (entityCtx.one(EntityType.class).equals(EntityType.PLAYER)) {
            Position targetPos = entityCtx.one(Position.class);

            // get grid of targetPosition
            for (Square s : grids) {
                if (Math.abs(s.pos.x - targetPos.x) <= 16 && Math.abs(s.pos.y - targetPos.y) <= 16) {
                    targetSquare = s;
                    break;
                }
            }
        }

        // Process ENEMY AI
        if (entityCtx.one(EntityType.class).equals(EntityType.ENEMY) && allObstaclesLocated && allAdjacentObstaclesLocated) {
            for (BehaviourEnum behaviour : entityCtx.all(BehaviourEnum.class)) {

                // Get context from entity
                Position position = entityCtx.one(Position.class);
                pos = new Position(position.x, position.y);
                Rotation rotation = entityCtx.one(Rotation.class);

                if (behaviour.equals(BehaviourEnum.MOVE_RANDOM)) {

                    rotation.angle = calculateAngle();

                    calculateShortestPath(pos, targetSquare.pos);

                    if (150 < Math.abs(pos.x - targetSquare.pos.x) || 150 < Math.abs(pos.y - targetSquare.pos.y)) {
                        // Move right
                        if (pos.x < moveNext.pos.x) {
                            pos.x += velocity;
                        } // Move left
                        else if (pos.x > moveNext.pos.x) {
                            pos.x -= velocity;
                        }

                        // Move down
                        if (pos.y < moveNext.pos.y) {
                            pos.y += velocity;
                        } // Move up
                        else if (pos.y > moveNext.pos.y) {
                            pos.y -= velocity;
                        }
                    }

                    position.x = pos.x;
                    position.y = pos.y;

                }

                if (behaviour.equals(HIT)) {

                    Health h = entityCtx.one(Health.class);

                    // Damage
                    h.addDamage(1);

                    // Check for destroyed
                    if (!h.isAlive()) {
                        entity.setDestroyed(true);
                    }

                    // Remove hit behaviour
                    context(entity).remove(behaviour);
                }
            }
        }
    }
}
